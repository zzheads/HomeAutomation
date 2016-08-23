package com.zzheads.HomeAutomation.model;//

import com.google.gson.*;
import com.zzheads.HomeAutomation.Application;
import com.zzheads.HomeAutomation.controller.RoomController;
import com.zzheads.HomeAutomation.exceptions.ApiErrorBadRequest;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
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

    public Room(Long id, String name, int squareFootage) {
        this.id = id;
        this.name = name;
        this.squareFootage = squareFootage;
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
        if (equipments == null) equipments = new ArrayList<>();
        equipments.add(equipment);
    }

    public void removeEquipment(Equipment equipment) {
        for (int i=0;i<equipments.size();i++) {
            if (Objects.equals(equipments.get(i).getId(), equipment.getId())) equipments.remove(i);
        }
    }

    public JsonArray getLinks() {
        JsonObject linkSelf = new JsonObject();
        linkSelf.addProperty("rel", "self");
        linkSelf.addProperty("href", Application.BASE_URL + "room/" + getId());
        JsonObject linkEquipment = new JsonObject();
        linkEquipment.addProperty("rel", "equipment");
        linkEquipment.addProperty("href", Application.BASE_URL + "room/" + getId() + "/equipment");
        JsonArray links = new JsonArray();
        links.add(linkSelf);
        links.add(linkEquipment);
        return links;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Room.class, new RoomSerializer()).setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public static class RoomSerializer implements JsonSerializer<Room> {
        @Override
        public JsonElement serialize(Room src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.addProperty("roomId", String.valueOf(src.getId()));
            result.addProperty("roomName", src.getName());
            result.addProperty("squareFootage", String.valueOf(src.getSquareFootage()));
            result.add("_links", src.getLinks());
            return result;
        }
    }

    public static class RoomDeserializer implements JsonDeserializer<Room> {
        @Override
        public Room deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            if (jsonObject.get("roomName") == null || jsonObject.get("squareFootage") == null)
                throw new ApiErrorBadRequest(400, String.format("%s (%s)", RoomController.EXPECTED_REQUEST_FORMAT, Thread.currentThread().getStackTrace()[1].toString()));
            Long id = jsonObject.get("roomId").getAsLong();
            String name = jsonObject.get("roomName").getAsString();
            int squareFootage = jsonObject.get("squareFootage").getAsInt();
            return new Room(id, name, squareFootage);
        }
    }

    public static class ListRoomDeserializer implements JsonDeserializer<List<Room>> {
        @Override
        public List<Room> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray array = json.getAsJsonArray();
            List<Room> rooms = new ArrayList<>();
            for (JsonElement e : array) {
                JsonObject o = (JsonObject) e;
                Long id = o.get("roomId").getAsLong();
                String name = o.get("roomName").getAsString();
                int squareFootage = o.get("squareFootage").getAsInt();

                rooms.add(new Room(id, name, squareFootage));
            }
            return rooms;
        }
    }

    public static class RoomTreeSerializer implements JsonSerializer<Room> {
        @Override
        public JsonElement serialize(Room src, Type typeOfSrc, JsonSerializationContext context) {
            Gson gson = new GsonBuilder().registerTypeAdapter(Equipment.class, new Equipment.EquipmentTreeSerializer()).create();
            JsonObject tree = new JsonObject();
            tree.addProperty("roomId", String.valueOf(src.getId()));
            tree.addProperty("roomName", src.getName());
            tree.addProperty("squareFootage", String.valueOf(src.getSquareFootage()));
            if (src.getEquipments() != null && src.getEquipments().size() > 0) {
                JsonArray equipments = new JsonArray();
                for (Equipment e : src.getEquipments())
                    equipments.add(gson.toJsonTree(e));
                tree.add("equipments", equipments);
            }
            return tree;
        }
    }

    public static Room fromJson(String jsonObject) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Room.class, new RoomDeserializer()).create();
        return gson.fromJson(jsonObject, Room.class);
    }

    public static String toJson(List<Room> rooms) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Room.class, new RoomSerializer()).setPrettyPrinting().create();
        return gson.toJson(rooms);
    }

    public static String toJsonTree(List<Room> rooms) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Room.class, new RoomTreeSerializer()).setPrettyPrinting().create();
        return gson.toJson(rooms);
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Room))
            return false;

        Room room = (Room) o;

        return getSquareFootage() == room.getSquareFootage() && (getId() != null ?
            getId().equals(room.getId()) :
            room.getId() == null && (getName() != null ?
                getName().equals(room.getName()) :
                room.getName() == null));

    }

    @Override public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + getSquareFootage();
        return result;
    }
}
