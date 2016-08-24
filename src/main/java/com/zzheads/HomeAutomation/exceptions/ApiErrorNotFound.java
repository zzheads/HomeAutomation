package com.zzheads.HomeAutomation.exceptions;//

import com.google.gson.*;

import java.lang.reflect.Type;

// HomeAutomation
// com.zzheads.HomeAutomation.exceptions created by zzheads on 21.08.2016.
//
public class ApiErrorNotFound extends ApiError {

    public ApiErrorNotFound(int status, String message) {
        super (status, message);
    }

    public ApiErrorNotFound(ApiError e) {
        this.setStatus(e.getStatus());
        this.setMessage(e.getMessage());
        this.setPath(e.getPath());
        this.setStackTrace(e.getStackTrace());
    }

    public static class ApiErrorNotFoundSerializer extends ApiErrorSerializer {
        public JsonElement serialize(ApiErrorNotFound src, Type typeOfSrc, JsonSerializationContext context) {
            return super.serialize(src, typeOfSrc, context);
        }
    }

    public static class ApiErrorNotFoundDeserializer extends ApiErrorDeserializer {
        public ApiErrorNotFound deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ApiError e = super.deserialize(json, typeOfT, context);
            return new ApiErrorNotFound(e);
        }
    }

    public String toJson() {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(ApiErrorNotFound.class, new ApiErrorNotFoundSerializer())
            .registerTypeAdapter(ApiErrorNotFound.class, new ApiErrorNotFoundDeserializer())
            .setPrettyPrinting()
            .create();
        return gson.toJson(this, ApiErrorNotFound.class);
    }

    public static ApiErrorNotFound fromJson(String jsonString) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(ApiErrorNotFound.class, new ApiErrorNotFoundSerializer())
            .registerTypeAdapter(ApiErrorNotFound.class, new ApiErrorNotFoundDeserializer())
            .create();
        return gson.fromJson(jsonString, ApiErrorNotFound.class);
    }
}
