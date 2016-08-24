package com.zzheads.HomeAutomation.model;//

import com.fasterxml.jackson.core.*;
import com.google.gson.*;
import com.google.gson.JsonParseException;
import com.zzheads.HomeAutomation.Application;
import com.zzheads.HomeAutomation.controller.ControlController;
import com.zzheads.HomeAutomation.controller.EquipmentController;
import com.zzheads.HomeAutomation.controller.RoomController;
import com.zzheads.HomeAutomation.exceptions.ApiErrorBadRequest;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import javax.persistence.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// HomeAutomation
// com.zzheads.HomeAutomation.model created by zzheads on 19.08.2016.
//
@Entity
public class Control {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal value;
    @ManyToOne
    private Equipment equipment;

    public Control(String name, BigDecimal value, Equipment equipment) {
        this.name = name;
        this.value = value;
        this.equipment = equipment;
    }

    public Control() {
    }

    public Control(String name) {
        this.name = name;
    }

    // JSON constructor
    public Control(Map<String, String> map) {
        name = map.get("controlName");
    }

    public Control(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Control(Long controlId, String controlName, BigDecimal controlValue) {
        this.id = controlId;
        this.name = controlName;
        this.value = controlValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void setValue(String string) {
        double d = Double.parseDouble(formattedString(string));
        this.value = BigDecimal.valueOf(d);
    }

    public static String formattedString (String req) {
        String result="";
        for (char c : req.toCharArray()) {
            if (Character.isDigit(c) || c=='.' || c==',') result+=c;
        }
        return result;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    private String getParentLink() {
        return Application.BASE_URL + "room/" + getEquipment().getRoom().getId() + "/equipment/" + getEquipment().getId();
    }

    public JsonArray getLinks() {
        JsonObject linkSelf = new JsonObject();
        linkSelf.addProperty("rel", "self");
        linkSelf.addProperty("href", getParentLink() + "/control/" + getId());
        JsonObject linkParent = new JsonObject();
        linkParent.addProperty("rel", "parent");
        linkParent.addProperty("href", getParentLink());
        JsonObject linkValue = new JsonObject();
        linkValue.addProperty("rel", "value");
        linkValue.addProperty("href", getParentLink() + "/control/" + getId() + "/value");
        JsonArray links = new JsonArray();
        links.add(linkSelf);
        links.add(linkParent);
        links.add(linkValue);
        return links;
    }

    public JsonArray getValueLinks() {
        JsonObject linkSelf = new JsonObject();
        linkSelf.addProperty("rel", "self");
        linkSelf.addProperty("href", getParentLink() + "/control/" + getId() + "/value");
        JsonObject linkParent = new JsonObject();
        linkParent.addProperty("rel", "parent");
        linkParent.addProperty("href", getParentLink() + "/control/" + getId());
        JsonArray links = new JsonArray();
        links.add(linkSelf);
        links.add(linkParent);
        return links;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Control.class, new ControlSerializer()).setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public String toJsonValue() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Control.class, new ControlValueSerializer()).setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public static class ControlSerializer implements JsonSerializer<Control> {
        @Override
        public JsonElement serialize(Control src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.addProperty("controlId", String.valueOf(src.getId()));
            result.addProperty("controlName", src.getName());
            result.add("_links", src.getLinks());
            return result;
        }
    }

    public static class ControlDeserializer implements JsonDeserializer<Control> {
        @Override public Control deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            if (jsonObject.get("controlName") == null)
                throw new ApiErrorBadRequest(400, String.format("%s (%s)", ControlController.EXPECTED_REQUEST_FORMAT, Thread.currentThread().getStackTrace()[1].toString()));

            String name = jsonObject.get("controlName").getAsString();
            if (jsonObject.has("controlId") && !Objects.equals(jsonObject.get("controlId").getAsString(), "null")) {
                Long id = jsonObject.get("controlId").getAsLong();
                return new Control(id, name);
            }
            return new Control(name);
        }
    }

    public static class ListControlSerializer implements JsonSerializer<List<Control>> {
        @Override public JsonElement serialize(List<Control> src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray result = new JsonArray();
            for (Control c : src) {
                JsonObject object = new JsonObject();
                object.addProperty("controlId", String.valueOf(c.getId()));
                object.addProperty("controlName", c.getName());
                object.add("_links", c.getLinks());
                result.add(object);
            }
            return result;
        }
    }

    public static class ListControlDeserializer implements JsonDeserializer<List<Control>> {
        @Override public List<Control> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray array = json.getAsJsonArray();
            List<Control> result = new ArrayList<>();
            for (JsonElement e : array) {
                JsonObject jsonObject = e.getAsJsonObject();
                if (jsonObject.get("controlName") == null)
                    throw new ApiErrorBadRequest(400, String.format("%s (%s)", ControlController.EXPECTED_REQUEST_FORMAT,
                        Thread.currentThread().getStackTrace()[1].toString()));

                String name = jsonObject.get("controlName").getAsString();
                if (jsonObject.has("controlId") && !Objects.equals(jsonObject.get("controlId").getAsString(), "null")) {
                    Long id = jsonObject.get("controlId").getAsLong();
                    result.add(new Control(id, name));
                } else {
                    result.add(new Control(name));
                }
            }
            return result;
        }
    }

    public static class ControlValueSerializer implements JsonSerializer<Control> {
        @Override
        public JsonElement serialize(Control src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.addProperty("value", String.valueOf(src.getValue().doubleValue()));
            result.add("_links", src.getValueLinks());
            return result;
        }
    }

    public static class ControlValueDeserializer implements JsonDeserializer<BigDecimal> {
        @Override
        public BigDecimal deserialize(JsonElement json, Type typeOfSrc, JsonDeserializationContext context) throws JsonParseException {
            return json.getAsBigDecimal();
        }
    }

    public static class ControlTreeSerializer implements JsonSerializer<Control> {
        @Override
        public JsonElement serialize(Control src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.addProperty("controlId", String.valueOf(src.getId()));
            result.addProperty("controlName", src.getName());
            if (src.getValue() != null)
                result.addProperty("value", String.valueOf(src.getValue().doubleValue()));
            return result;
        }
    }

    public static String toJson(List<Control> controls) {
        Gson gson = new GsonBuilder().registerTypeAdapter(List.class, new ListControlSerializer()).create();
        return gson.toJson(controls, List.class);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Control)) return false;

        Control control = (Control) o;

        if (getId() != null ? !getId().equals(control.getId()) : control.getId() != null) return false;
        if (!getName().equals(control.getName())) return false;
        if (getValue() != null ? !getValue().equals(control.getValue()) : control.getValue() != null) return false;
        if (getEquipment()!=null) {
            return control.getEquipment() != null && (Objects.equals(getEquipment().getId(), control.getEquipment().getId()));
        }
        return (control.getEquipment() == null);
    }

    @Override public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + getName().hashCode();
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        result = 31 * result + (getEquipment() != null ? getEquipment().hashCode() : 0);
        return result;
    }
}
