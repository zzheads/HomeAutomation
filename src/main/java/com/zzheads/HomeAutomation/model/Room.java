package com.zzheads.HomeAutomation.model;//

import com.google.gson.*;
import com.zzheads.HomeAutomation.Application;
import com.zzheads.HomeAutomation.controller.RoomController;
import com.zzheads.HomeAutomation.exceptions.ApiErrorBadRequest;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.lang.reflect.Type;
import java.util.*;

// HomeAutomation
// com.zzheads.HomeAutomation.model created by zzheads on 20.08.2016.
//
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int squareFootage;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Equipment> equipments = new ArrayList<>();

    public Room() {
    }

    public Room (JsonObject jsonObject) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Room.class, new RoomDeserializer()).create();
        name = gson.fromJson(jsonObject, Room.class).getName();
        squareFootage = gson.fromJson(jsonObject, Room.class).getSquareFootage();
    }

    public Room(Map<String, String> map) {
        name = map.get("roomName");
        squareFootage = Integer.parseInt(map.get("squareFootage"));
    }

    public Room(String name, int squareFootage) {
        this.name = name;
        this.squareFootage = squareFootage;
    }

    public Room(String name, int squareFootage, List<Equipment> equipments) {
        this.name = name;
        this.squareFootage = squareFootage;
        this.equipments = equipments;
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

    public int getSquareFootage() {
        return squareFootage;
    }

    public void setSquareFootage(int squareFootage) {
        this.squareFootage = squareFootage;
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }

    public void addEquipment(Equipment equipment) {
        equipments.add(equipment);
    }

    public void removeEquipment(Equipment equipment) {
        for (int i=0;i<equipments.size();i++) {
            if (Objects.equals(equipments.get(i).getId(), equipment.getId())) equipments.remove(i);
        }
    }

    private static class RoomSerializer implements JsonSerializer<Room> {
        @Override
        public JsonElement serialize(Room src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject linkSelf = new JsonObject();
            linkSelf.addProperty("rel", "self");
            linkSelf.addProperty("href", Application.BASE_URL + "room/" + src.getId());
            JsonObject linkEquipment = new JsonObject();
            linkEquipment.addProperty("rel", "equipment");
            linkEquipment.addProperty("href", Application.BASE_URL + "room/" + src.getId() + "/equipment");
            JsonObject result = new JsonObject();
            result.addProperty("roomId", String.valueOf(src.getId()));
            result.addProperty("roomName", src.getName());
            result.addProperty("squareFootage", String.valueOf(src.getSquareFootage()));
            JsonArray links = new JsonArray();
            links.add(linkSelf);
            links.add(linkEquipment);
            result.add("_links", links);
            return result;
        }
    }

    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Room.class, new RoomSerializer()).setPrettyPrinting().create();
        return gson.toJson(this);
    }

    @SuppressWarnings("unchecked")
    public static String toJson(List<Room> rooms) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Room.class, new RoomSerializer()).setPrettyPrinting().create();
        return gson.toJson(rooms);
    }

    private static class RoomDeserializer implements JsonDeserializer<Room> {
        @Override
        public Room deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            if (jsonObject.get("roomName") == null || jsonObject.get("squareFootage") == null)
                throw new ApiErrorBadRequest(400, String.format("%s (%s)", RoomController.EXPECTED_REQUEST_FORMAT, Thread.currentThread().getStackTrace()[1].toString()));
            String name = jsonObject.get("roomName").getAsString();
            int squareFootage = jsonObject.get("squareFootage").getAsInt();
            return new Room(name, squareFootage);
        }
    }

    public static Room fromJson(JsonObject jsonObject) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Room.class, new RoomDeserializer()).create();
        return gson.fromJson(jsonObject, Room.class);
    }

}
