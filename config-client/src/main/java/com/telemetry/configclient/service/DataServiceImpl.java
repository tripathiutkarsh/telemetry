package com.telemetry.configclient.service;

import com.telemetry.configclient.dao.Data;
import com.telemetry.configclient.exception.DeviceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.Random;
@Service
@RefreshScope
public class DataServiceImpl implements DataService {

    @Value("${device.id}")
    private String id;
    @Value("${device.location}")
    private String location;

    @Override
    public Data getData(String idParam) throws DeviceNotFoundException {
        System.out.println(idParam+" : "+id);
        if (idParam.equals(id)){

            Data dto = new Data();
            dto.setId(id);
            dto.setLocation(location);
            dto.setHumidity(getRandom(1, 100));
            dto.setTemperature(getRandom(1, 100));
            return dto;
        }
        else
            throw new DeviceNotFoundException("No such Device");

    }

    private String getRandom(int low, int high) {
        return String.valueOf(new Random().nextInt(high - low) + low);
    }
}