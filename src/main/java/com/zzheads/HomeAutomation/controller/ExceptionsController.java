package com.zzheads.HomeAutomation.controller;//

import com.google.gson.*;
import com.zzheads.HomeAutomation.exceptions.ApiError;
import com.zzheads.HomeAutomation.exceptions.ApiErrorBadRequest;
import com.zzheads.HomeAutomation.exceptions.ApiErrorNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

// HomeAutomation
// com.zzheads.HomeAutomation.controller created by zzheads on 21.08.2016.
//
@ControllerAdvice
public class ExceptionsController {
    @ExceptionHandler(ApiErrorNotFound.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody String handleNotFound(HttpServletRequest req, ApiErrorNotFound exc) {
        if (req.getPathInfo()!=null)
            exc.setPath(req.getPathInfo());
        else
            exc.setPath(req.getServletPath());
        return exc.toJson();
    }

    @ExceptionHandler(ApiErrorBadRequest.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleBadRequest(HttpServletRequest req, ApiErrorBadRequest exc) {
        if (req.getPathInfo()!=null)
            exc.setPath(req.getPathInfo());
        else
            exc.setPath(req.getServletPath());
        return exc.toJson();
    }
}
