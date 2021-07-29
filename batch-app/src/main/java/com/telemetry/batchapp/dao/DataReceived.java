package com.telemetry.batchapp.dao;

import com.telemetry.batchapp.model.Data;
import com.telemetry.batchapp.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataReceived extends Data implements Comparable<DataReceived> {

    private String timestamp;

    public DataReceived() {
    }

    public DataReceived(String id, String temperature, String humidity, String location, String timestamp) {
        super(id, temperature, humidity, location);
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(DataReceived o) {
        SimpleDateFormat s = new SimpleDateFormat(Constants.TIMESTAMP_FORMAT);
        Date ts = null;
        Date ots = null;
        try {
            ts = s.parse(getTimestamp());
            ots = s.parse(o.getTimestamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ts.compareTo(ots);
    }
}
