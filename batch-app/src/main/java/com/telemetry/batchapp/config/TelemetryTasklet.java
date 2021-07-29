package com.telemetry.batchapp.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.telemetry.batchapp.model.Data;
import com.telemetry.batchapp.dao.DataReceived;
import com.telemetry.batchapp.utils.Constants;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.PriorityQueue;

public class TelemetryTasklet implements Tasklet {
    private static PriorityQueue<DataReceived> cache = new PriorityQueue<DataReceived>();
    @Value("${rest.api.url}")
    private String apiUrl;
    @Autowired
    private RestTemplate restTemplate;
    private DataReceived data;
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        write(fetchData());
        return RepeatStatus.FINISHED;
    }

    private DataReceived fetchData(){
        ResponseEntity<Data> response = restTemplate.getForEntity(apiUrl, Data.class);
        Data data = response.getBody();
        SimpleDateFormat s = new SimpleDateFormat(Constants.TIMESTAMP_FORMAT);
        Date date = new Date();
        return new DataReceived(data.getId(), data.getTemperature(), data.getHumidity(), data.getLocation(),s.format(date));
    }

    private void write(DataReceived data) throws JsonProcessingException {
        if (cache.size() < 5){
            cache.add(data);
        }
        else{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            String arrayToJson = objectMapper.writeValueAsString(cache.toArray());
            cache.clear();
            String filename = Constants.JSON_PATH + String.valueOf(System.currentTimeMillis()) + Constants.JSON;
            try (FileWriter file = new FileWriter(filename)) {
                file.write(arrayToJson);
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
