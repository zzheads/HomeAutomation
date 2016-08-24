package com.zzheads.HomeAutomation.exceptions;//

import com.google.gson.*;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Date;

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
        this.path = path;
    }

    public ApiError() {
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

    public static class ApiErrorSerializer implements JsonSerializer<ApiError> {
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

    public static class ApiErrorDeserializer implements JsonDeserializer<ApiError> {
        @Override public ApiError deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonError = json.getAsJsonObject();
            String message = jsonError.get("message").getAsString();
            int status = jsonError.get("status").getAsInt();
            String path = jsonError.get("path").getAsString();
            return new ApiError(status, message, path);
        }
    }

    public String toJson() {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(ApiError.class, new ApiErrorSerializer())
            .registerTypeAdapter(ApiError.class, new ApiErrorDeserializer())
            .setPrettyPrinting()
            .create();
        return gson.toJson(this, ApiError.class);
    }

//    public static ApiError fromJson(String jsonString) {
//        Gson gson = new GsonBuilder()
//            .registerTypeAdapter(ApiError.class, new ApiErrorSerializer())
//            .registerTypeAdapter(ApiError.class, new ApiErrorDeserializer())
//            .create();
//        return gson.fromJson(jsonString, ApiError.class);
//    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ApiError))
            return false;

        ApiError apiError = (ApiError) o;

        return getStatus() == apiError.getStatus() && (getMessage() != null ?
            getMessage().equals(apiError.getMessage()) :
            apiError.getMessage() == null && (getPath() != null ?
                getPath().equals(apiError.getPath()) :
                apiError.getPath() == null));

    }

    @Override public int hashCode() {
        int result = getStatus();
        result = 31 * result + (getMessage() != null ? getMessage().hashCode() : 0);
        result = 31 * result + (getPath() != null ? getPath().hashCode() : 0);
        return result;
    }
}
