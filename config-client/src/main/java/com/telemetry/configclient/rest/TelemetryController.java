package com.telemetry.configclient.rest;

import com.telemetry.configclient.dao.Data;
import com.telemetry.configclient.exception.DeviceNotFoundException;
import com.telemetry.configclient.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trucks")
public class TelemetryController {
    @Autowired
    DataService dataService;

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public Data getData(@PathVariable String id) throws DeviceNotFoundException {
        Data data = dataService.getData(id);
        return data;
    }
}
