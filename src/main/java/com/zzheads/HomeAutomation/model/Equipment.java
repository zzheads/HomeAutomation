package com.zzheads.HomeAutomation.model;//

import com.google.gson.*;
import com.zzheads.HomeAutomation.Application;
import com.zzheads.HomeAutomation.controller.RoomController;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.lang.reflect.Type;
import java.util.*;

// HomeAutomation
// com.zzheads.HomeAutomation.model created by zzheads on 20.08.2016.
//
@Entity
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL)
    private List<Control> controls;
    @ManyToOne
    private Room room;

    public Equipment() {
    }

    // JSON constructor
    public Equipment(Map<String, String> map) {
        name = map.get("equipmentName");
    }

    public Equipment(String name, Room room, List<Control> controls) {
        this.name = name;
        this.room = room;
        this.controls = controls;
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

    public List<Control> getControls() {
        return controls;
    }

    public void setControls(List<Control> controls) {
        this.controls = controls;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void addControl(Control control) {
        controls.add(control);
    }

    public void removeControl(Control control) {
        for (int i=0;i<controls.size();i++) {
            if (Objects.equals(controls.get(i).getId(), control.getId())) controls.remove(i);
        }
    }

    private static class EquipmentSerializer implements JsonSerializer<Equipment> {
        @Override
        public JsonElement serialize(Equipment src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject linkSelf = new JsonObject();
            linkSelf.addProperty("rel", "self");
            linkSelf.addProperty("href", Application.BASE_URL + "room/" + src.getRoom().getId() + "/equipment/" + src.getId());
            JsonObject linkParent = new JsonObject();
            linkParent.addProperty("rel", "parent");
            linkParent.addProperty("href", Application.BASE_URL + "room/" + src.getRoom().getId());
            JsonObject linkControl = new JsonObject();
            linkControl.addProperty("rel", "control");
            linkControl.addProperty("href", Application.BASE_URL + "room/" + src.getId() + "/equipment/" + src.getId() + "/control");
            JsonObject result = new JsonObject();
            result.addProperty("equipmentId", String.valueOf(src.getId()));
            result.addProperty("equipmentName", src.getName());
            JsonArray links = new JsonArray();
            links.add(linkSelf);
            links.add(linkParent);
            links.add(linkControl);
            result.add("_links", links);
            return result;
        }
    }

    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Equipment.class, new EquipmentSerializer()).setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public static String toJson(List<Equipment> equipments) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Equipment.class, new EquipmentSerializer()).setPrettyPrinting().create();
        return gson.toJson(equipments);
    }

}
