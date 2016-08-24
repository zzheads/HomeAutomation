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

    @SuppressWarnings("unchecked")
    public static List<Room> fromJson (JsonElement jsonElement) {
        Gson gson = new GsonBuilder().registerTypeAdapter(List.class, new ListRoomDeserializer()).setPrettyPrinting().create();
        return gson.fromJson(jsonElement, List.class);
    }

    public static String toJson (List<Room> rooms)
    {
        Gson gson = new GsonBuilder().registerTypeAdapter(List.class, new ListRoomSerializer()).setPrettyPrinting().create();
        return gson.toJson(rooms, List.class);
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
            String name = jsonObject.get("roomName").getAsString();
            int squareFootage = jsonObject.get("squareFootage").getAsInt();
            if (!Objects.equals(jsonObject.get("roomId").getAsString(), "null")) {
                Long id = jsonObject.get("roomId").getAsLong();
                return new Room(id, name, squareFootage);
            }
            return new Room(name, squareFootage);
        }
    }

    public static class ListRoomSerializer implements JsonSerializer<List<Room>> {
        @Override
        public JsonArray serialize(List<Room> src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray rooms = new JsonArray();
            for (Room room : src) {
                JsonObject jsonRoom = new JsonObject();
                jsonRoom.addProperty("roomId", String.valueOf(room.getId()));
                jsonRoom.addProperty("roomName", room.getName());
                jsonRoom.addProperty("squareFootage", String.valueOf(room.getSquareFootage()));
                jsonRoom.add("_links", room.getLinks());
                if (room.getEquipments() != null) {
                    JsonArray equipments = new JsonArray();
                    for (int j = 0; j < room.getEquipments().size(); j++) {
                        Equipment equipment = room.getEquipments().get(j);
                        JsonObject jsonEquipment = new JsonObject();
                        jsonEquipment.addProperty("equipmentId", String.valueOf(equipment.getId()));
                        jsonEquipment.addProperty("equipmentName", equipment.getName());
                        jsonEquipment.add("_links", equipment.getLinks());
                        if (equipment.getControls() != null) {
                            JsonArray controls = new JsonArray();
                            for (int k = 0; k < equipment.getControls().size(); k++) {
                                Control control = equipment.getControls().get(k);
                                JsonObject jsonControl = new JsonObject();
                                jsonControl.addProperty("controlId", String.valueOf(control.getId()));
                                jsonControl.addProperty("controlName", control.getName());
                                if (control.getValue() != null) {
                                    jsonControl.addProperty("value", String.valueOf(control.getValue()));
                                }
                                jsonControl.add("_links", control.getLinks());
                                controls.add(jsonControl);
                            }
                            if (controls.size() > 0) {
                                jsonEquipment.add("controls", controls);
                            }
                        }
                        equipments.add(jsonEquipment);
                    }
                    if (equipments.size() > 0) {
                        jsonRoom.add("equipments", equipments);
                    }
                }
                rooms.add(jsonRoom);
            }
            return rooms;
        }
    }

    public static class ListRoomDeserializer implements JsonDeserializer<List<Room>> {
        @Override
        public List<Room> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            List<Room> rooms = new ArrayList<>();
            JsonArray arrayRooms = json.getAsJsonArray();
            for (int i=0;i<arrayRooms.size();i++) {
                JsonObject jsonRoom = arrayRooms.get(i).getAsJsonObject();
                Long roomId = jsonRoom.get("roomId").getAsLong();
                String roomName = jsonRoom.get("roomName").getAsString();
                int squareFootage = jsonRoom.get("squareFootage").getAsInt();
                rooms.add(new Room(roomId, roomName, squareFootage));
                Room currentRoom = rooms.get(i);
                // possible there is equipments
                if (jsonRoom.has("equipments") && !jsonRoom.get("equipments").isJsonNull()) {
                    JsonArray arrayEquipment = jsonRoom.get("equipments").getAsJsonArray();
                    for (int j = 0; j < arrayEquipment.size(); j++) {
                        JsonObject jsonEquipment = arrayEquipment.get(j).getAsJsonObject();
                        Long equipmentId = jsonEquipment.get("equipmentId").getAsLong();
                        String equipmentName = jsonEquipment.get("equipmentName").getAsString();
                        rooms.get(i).addEquipment(new Equipment(equipmentId, equipmentName));
                        Equipment currentEquipment = currentRoom.getEquipments().get(j);
                        currentEquipment.setRoom(currentRoom);
                        // possible there is controls
                        JsonArray arrayControl = jsonEquipment.get("controls").getAsJsonArray();
                        for (int k = 0; k < arrayControl.size(); k++) {
                            JsonObject jsonControl = arrayControl.get(k).getAsJsonObject();
                            Long controlId = jsonControl.get("controlId").getAsLong();
                            String controlName = jsonControl.get("controlName").getAsString();
                            if (jsonControl.has("value")) {
                                BigDecimal controlValue = jsonControl.get("value").getAsBigDecimal();
                                rooms.get(i).getEquipments().get(j)
                                    .addControl(new Control(controlId, controlName, controlValue));
                            } else {
                                rooms.get(i).getEquipments().get(j)
                                    .addControl(new Control(controlId, controlName));
                            }
                            Control currentControl = currentEquipment.getControls().get(k);
                            currentControl.setEquipment(currentEquipment);
                        }
                    }
                }
            }
            return rooms;
        }
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
