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
        Gson gson = new GsonBuilder().registerTypeAdapter(ApiErrorNotFound.class, new ApiErrorSerializer()).setPrettyPrinting().create();
        exc.setPath(req.getServletPath());
        return gson.toJson(exc);
    }

    @ExceptionHandler(ApiErrorBadRequest.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleBadRequest(HttpServletRequest req, ApiErrorBadRequest exc) {
        Gson gson = new GsonBuilder().registerTypeAdapter(ApiErrorBadRequest.class, new ApiErrorSerializer()).setPrettyPrinting().create();
        exc.setPath(req.getServletPath());
        return gson.toJson(exc);
    }

    private class ApiErrorSerializer implements JsonSerializer<ApiError> {
        @Override
        public JsonElement serialize(ApiError src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            Date date = new Date();
            result.addProperty("timestamp", String.valueOf(new Timestamp(date.getTime())));
            result.addProperty("status", String.valueOf(src.getStatus()));
            if (src.getStatus() == 404) {
                result.addProperty("error", "Not Found");
            } else {
                result.addProperty("error", "Bad Request");
            }
            result.addProperty("exception", typeOfSrc.getTypeName());
            result.addProperty("message", src.getMessage());
            result.addProperty("path", src.getPath());
            return result;
        }
    }
}
