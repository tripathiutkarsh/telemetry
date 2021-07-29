package com.telemetry.configclient.exception;

public class DeviceNotFoundException extends Exception{
    public DeviceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeviceNotFoundException(Throwable cause) {
        super(cause);
    }

    public DeviceNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DeviceNotFoundException() {
    }

    public DeviceNotFoundException(String message) {
        super(message);
    }
}
