package com.zzheads.HomeAutomation.exceptions;//

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

// HomeAutomation
// com.zzheads.HomeAutomation.exceptions created by zzheads on 21.08.2016.
//
public class ApiError extends RuntimeException {
    private int status;
    private String message;
    private String path;

    public ApiError(int status, String message) {
        super (message);
        this.status = status;
        this.message = message;
    }

    public ApiError(int status, String message, String path) {
        super (message);
        this.status = status;
        this.message = message;
    }

    public ApiError(String message) {
        super(message);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
