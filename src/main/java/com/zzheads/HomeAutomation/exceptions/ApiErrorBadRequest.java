package com.zzheads.HomeAutomation.exceptions;//

import com.google.gson.*;
import com.zzheads.HomeAutomation.model.Control;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

// HomeAutomation
// com.zzheads.HomeAutomation.exceptions created by zzheads on 21.08.2016.
//
public class ApiErrorBadRequest extends ApiError {

    public ApiErrorBadRequest(int status, String message) {
        super (status, message);
    }

    public ApiErrorBadRequest(ApiError e) {
        this.setStatus(e.getStatus());
        this.setMessage(e.getMessage());
        this.setPath(e.getPath());
        this.setStackTrace(e.getStackTrace());
    }

    public static class ApiErrorBadRequestSerializer extends ApiErrorSerializer {
        public JsonElement serialize(ApiErrorBadRequest src, Type typeOfSrc, JsonSerializationContext context) {
            return super.serialize(src, typeOfSrc, context);
        }
    }

    public static class ApiErrorBadRequestDeserializer extends ApiErrorDeserializer {
        public ApiErrorBadRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ApiError e = super.deserialize(json, typeOfT, context);
            return new ApiErrorBadRequest(e);
        }
    }

    public String toJson() {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(ApiErrorBadRequest.class, new ApiErrorBadRequestSerializer())
            .registerTypeAdapter(ApiErrorBadRequest.class, new ApiErrorBadRequestDeserializer())
            .setPrettyPrinting()
            .create();
        return gson.toJson(this, ApiErrorBadRequest.class);
    }

    public static ApiErrorBadRequest fromJson(String jsonString) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(ApiErrorBadRequest.class, new ApiErrorBadRequestSerializer())
            .registerTypeAdapter(ApiErrorBadRequest.class, new ApiErrorBadRequestDeserializer())
            .create();
        return gson.fromJson(jsonString, ApiErrorBadRequest.class);
    }

}
