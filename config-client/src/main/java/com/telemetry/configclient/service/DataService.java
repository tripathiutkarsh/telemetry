package com.telemetry.configclient.service;

import com.telemetry.configclient.dao.Data;
import com.telemetry.configclient.exception.DeviceNotFoundException;

public interface DataService {
    Data getData(String id) throws DeviceNotFoundException;

}
