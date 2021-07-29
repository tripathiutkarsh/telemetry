package com.telemetry.batchapp.dao;

import org.springframework.batch.item.ItemProcessor;

public class Processor implements ItemProcessor<DataReceived, DataReceived> {
    @Override
    public DataReceived process(DataReceived data) throws Exception {
        return Integer.parseInt(data.getTemperature()) > 45 ? data : null;
    }
}
