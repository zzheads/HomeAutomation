package com.zzheads.HomeAutomation.model;//

import com.zzheads.HomeAutomation.controller.RoomController;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import javax.persistence.*;
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
        String result="";
        for (char c : string.toCharArray()) {
            if (Character.isDigit(c) || c=='.' || c==',') result+=c;
        }
        double d = Double.parseDouble(result);
        this.value = BigDecimal.valueOf(d);
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    // True - links for control, false - for value
    @SuppressWarnings("unchecked")
    private List<Map> getLinks(boolean linksForControlOrValue) {
        List<Map> links = new ArrayList<>();
        final String parentString = RoomController.BASE_URL+"room/"+equipment.getRoom().getId()+"/equipment";
        final String selfString = parentString + "/"+equipment.getId()+"/control/"+id;
        final String valueString = selfString + "/value";

        links.add(new HashMap<>());
        links.get(0).put("rel", "self");
        links.get(0).put("href", selfString);
        links.add(new HashMap<>());
        links.get(1).put("rel", "parent");
        links.get(1).put("href", parentString);
        if (linksForControlOrValue) {
            links.add(new HashMap<>());
            links.get(2).put("rel", "value");
            links.get(2).put("href", valueString);
        }
        return links;
    }

    public Map Json() {
        Map <String, Object> res = new HashMap<>();
        res.put("controlId", String.valueOf(id));
        res.put("controlName", name);
        res.put("_links", getLinks(true));
        return res;
    }

    public Map jsonValue() {
        Map <String, Object> res = new HashMap<>();
        res.put("value", value.toString());
        res.put("_links", getLinks(false));
        return res;
    }

    @SuppressWarnings("unchecked")
    public static List Json(List<Control> controls) {
        List<Map> res = new ArrayList<>();

        for (int i=0; i<controls.size(); i++) {
            res.add(new HashMap<>());
            res.get(i).put("controlId", String.valueOf(controls.get(i).getId()));
            res.get(i).put("controlName", controls.get(i).getName());
            res.get(i).put("_links", controls.get(i).getLinks(true));
        }
        return res;
    }


}
