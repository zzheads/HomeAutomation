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

    public Equipment(String s) {
        this.name = s;
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
        if (controls == null) controls = new ArrayList<>();
        controls.add(control);
    }

    public void removeControl(Control control) {
        for (int i=0;i<controls.size();i++) {
            if (Objects.equals(controls.get(i).getId(), control.getId())) controls.remove(i);
        }
    }

    public JsonArray getLinks() {
        JsonObject linkSelf = new JsonObject();
        linkSelf.addProperty("rel", "self");
        linkSelf.addProperty("href", Application.BASE_URL + "room/" + getRoom().getId() + "/equipment/" + getId());
        JsonObject linkParent = new JsonObject();
        linkParent.addProperty("rel", "parent");
        linkParent.addProperty("href", Application.BASE_URL + "room/" + getRoom().getId());
        JsonObject linkControl = new JsonObject();
        linkControl.addProperty("rel", "control");
        linkControl.addProperty("href", Application.BASE_URL + "room/" + getId() + "/equipment/" + getId() + "/control");
        JsonArray links = new JsonArray();
        links.add(linkSelf);
        links.add(linkParent);
        links.add(linkControl);
        return links;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Equipment.class, new EquipmentSerializer()).setPrettyPrinting().create();
        return gson.toJson(this);
    }

    private static class EquipmentSerializer implements JsonSerializer<Equipment> {
        @Override
        public JsonElement serialize(Equipment src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.addProperty("equipmentId", String.valueOf(src.getId()));
            result.addProperty("equipmentName", src.getName());
            result.add("_links", src.getLinks());
            return result;
        }
    }

    public static class EquipmentTreeSerializer implements JsonSerializer<Equipment> {
        @Override
        public JsonElement serialize(Equipment src, Type typeOfSrc, JsonSerializationContext context) {
            Gson gson = new GsonBuilder().registerTypeAdapter(Control.class, new Control.ControlTreeSerializer()).create();
            JsonObject result = new JsonObject();
            result.addProperty("equipmentId", String.valueOf(src.getId()));
            result.addProperty("equipmentName", src.getName());
            if (src.getControls() != null && src.getControls().size() > 0) {
                JsonArray controls = new JsonArray();
                for (Control c : src.getControls())
                    controls.add(gson.toJsonTree(c));
                result.add("controls", controls);
            }
            return result;
        }
    }

    public static String toJson(List<Equipment> equipments) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Equipment.class, new EquipmentSerializer()).setPrettyPrinting().create();
        return gson.toJson(equipments);
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Equipment))
            return false;

        Equipment equipment = (Equipment) o;

        return getId() != null ?
            getId().equals(equipment.getId()) :
            equipment.getId() == null && (getName() != null ?
                getName().equals(equipment.getName()) :
                equipment.getName() == null);

    }

    @Override public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }
}
