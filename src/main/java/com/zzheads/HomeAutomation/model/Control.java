package com.zzheads.HomeAutomation.model;//

import com.google.gson.*;
import com.zzheads.HomeAutomation.Application;
import com.zzheads.HomeAutomation.controller.RoomController;
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

    // JSON constructor
    public Control(Map<String, String> map) {
        name = map.get("controlName");
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

    private static class ControlSerializer implements JsonSerializer<Control> {
        @Override
        public JsonElement serialize(Control src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.addProperty("controlId", String.valueOf(src.getId()));
            result.addProperty("controlName", src.getName());
            result.add("_links", src.getLinks());
            return result;
        }
    }

    private static class ControlValueSerializer implements JsonSerializer<Control> {
        @Override
        public JsonElement serialize(Control src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.addProperty("value", String.valueOf(src.getValue().doubleValue()));
            result.add("_links", src.getValueLinks());
            return result;
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

    public static String toJson(List<Control> equipments) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Control.class, new ControlSerializer()).setPrettyPrinting().create();
        return gson.toJson(equipments);
    }

    public static String toJsonValue(List<Control> equipments) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Control.class, new ControlValueSerializer()).setPrettyPrinting().create();
        return gson.toJson(equipments);
    }
}
