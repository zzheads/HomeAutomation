package com.zzheads.HomeAutomation.model;//

import com.zzheads.HomeAutomation.controller.RoomController;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        this.value = BigDecimal.valueOf(Double.parseDouble(result));
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    // True - links for control, false - for value
    private Map[] getLinks(boolean linksForControlOrValue) {
        Map[] links;
        Map <String, String> linksSelf = new HashMap<>();
        linksSelf.put("rel", "self");
        linksSelf.put("href", RoomController.BASE_URL+"room/"+equipment.getRoom().getId()+"/equipment/"+equipment.getId()+"/control/"+id);
        Map <String, String> linksParents = new HashMap<>();
        linksParents.put("rel", "parent");
        linksParents.put("href", RoomController.BASE_URL+"room/"+equipment.getRoom().getId()+"/equipment");
        Map <String, String> linksValue = new HashMap<>();
        linksValue.put("rel", "value");
        linksValue.put("href", RoomController.BASE_URL+"room/"+equipment.getRoom().getId()+"/equipment/"+equipment.getId()+"/control/"+id+"/value");
        if (linksForControlOrValue) {
            links = new Map[3];
            links[0] = linksSelf;
            links[1] = linksParents;
            links[2] = linksValue;
        } else {
            links = new Map[2];
            links[0] = linksSelf;
            links[1] = linksParents;
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
