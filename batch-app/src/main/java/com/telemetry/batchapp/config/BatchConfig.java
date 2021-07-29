package com.telemetry.batchapp.config;

import com.telemetry.batchapp.dao.DataReceived;
import com.telemetry.batchapp.dao.Processor;
import com.telemetry.batchapp.utils.Constants;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private Resource outputResource = new FileSystemResource(Constants.REPORT_PATH + System.currentTimeMillis() + Constants.CSV);
    @Autowired
    private JobBuilderFactory jobBuilder;
    @Autowired
    private StepBuilderFactory stepBuilder;

    @Bean
    public Step step1() {
        return stepBuilder.get("step1").tasklet(telemetryTasklet()).build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public TelemetryTasklet telemetryTasklet() {
        return new TelemetryTasklet();
    }

    @Bean
    public Job job() {
        return jobBuilder.get("read-data")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Job report(Step stepReport) {
        return jobBuilder.get("report-csv")
                .incrementer(new RunIdIncrementer())
                .flow(stepReport)
                .end()
                .build();
    }

    @Bean
    public Step stepReport() {
        return stepBuilder.get("stepReport")
                .<DataReceived, DataReceived>chunk(5)
                .reader(multiResourceItemReader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Processor processor() {
        return new Processor();
    }

    @Bean
    public MultiResourceItemReader<DataReceived> multiResourceItemReader() {
        MultiResourceItemReader<DataReceived> resourceItemReader = new MultiResourceItemReader<DataReceived>();
        resourceItemReader.setResources(files());
        resourceItemReader.setDelegate(reader());
        return resourceItemReader;
    }

    @Bean
    public JsonItemReader<DataReceived> reader() {
        JsonItemReader<DataReceived> delegate = new JsonItemReaderBuilder<DataReceived>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(DataReceived.class))
                .name("dataItemReader")
                .build();
        return delegate;
    }

    @Bean
    public FlatFileItemWriter<DataReceived> writer() {
        //Create writer instance
        FlatFileItemWriter<DataReceived> writer = new FlatFileItemWriter<>();

        //Set output file location
        writer.setResource(outputResource);

        //All job repetitions should "append" to same output file
        writer.setAppendAllowed(true);

        //Name field values sequence based on object properties
        writer.setLineAggregator(new DelimitedLineAggregator<DataReceived>() {
            {
                setDelimiter(Constants.COMMA);
                setFieldExtractor(new BeanWrapperFieldExtractor<DataReceived>() {
                    {
                        setNames(new String[]{
                                Constants.ID,
                                Constants.TEMPERATURE,
                                Constants.HUMIDITY,
                                Constants.LOCATION,
                                Constants.TIMESTAMP});
                    }
                });
            }
        });
        return writer;
    }

    private Resource[] files(){
        try (Stream<Path> walk = Files.walk(Paths.get(Constants.JSON_RESOURCES))) {
            List<FileSystemResource> result = walk.filter(Files::isRegularFile)
                    .map(x -> new FileSystemResource(x.toString())).collect(Collectors.toList());

            return result.toArray(new Resource[result.size()]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Resource[0];
    }
}
