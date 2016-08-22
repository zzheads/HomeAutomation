package com.zzheads.HomeAutomation.exceptions;//

import com.google.gson.Gson;
import com.zzheads.HomeAutomation.model.Control;

import java.util.Map;

// HomeAutomation
// com.zzheads.HomeAutomation.exceptions created by zzheads on 21.08.2016.
//
public class ApiErrorBadRequest extends ApiError {
    private int status;
    private String message;

    public ApiErrorBadRequest(int status, String message) {
        super (message);
        this.status = status;
        this.message = message;
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
}
