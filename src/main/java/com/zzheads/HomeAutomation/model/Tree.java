package com.zzheads.HomeAutomation.model;//

import com.google.gson.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.model created by zzheads on 23.08.2016.
//
public class Tree {

    private static Gson gson = new GsonBuilder()
        .registerTypeAdapter(List.class, new TreeSerializer())
        .registerTypeAdapter(List.class, new TreeDeserializer())
        .setPrettyPrinting()
        .create();

    @SuppressWarnings("unchecked")
    public static List<Room> fromJson (JsonElement jsonElement) {
        return gson.fromJson(jsonElement, List.class);
    }

    public static String toJson (List<Room> rooms) {
        return gson.toJson(rooms, List.class);
    }

    public static class TreeSerializer implements JsonSerializer<List<Room>> {
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

    public static class TreeDeserializer implements JsonDeserializer<List<Room>> {
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
}
