package com.zzheads.HomeAutomation;//

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzheads.HomeAutomation.exceptions.ApiError;
import com.zzheads.HomeAutomation.exceptions.DaoException;
import com.zzheads.HomeAutomation.model.Control;
import com.zzheads.HomeAutomation.model.Equipment;
import com.zzheads.HomeAutomation.model.Room;
import com.zzheads.HomeAutomation.service.ControlService;
import com.zzheads.HomeAutomation.service.EquipmentService;
import com.zzheads.HomeAutomation.service.RoomService;
import com.zzheads.HomeAutomation.testing.ApiClient;
import com.zzheads.HomeAutomation.testing.ApiResponse;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// HomeAutomation
// com.zzheads.HomeAutomation created by zzheads on 23.08.2016.
//
@SuppressWarnings({"Duplicates", "ThrowableInstanceNeverThrown",
        "ThrowableResultOfMethodCallIgnored", "SpringJavaAutowiringInspection"})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class IntegrityTest {

    @Autowired
    private RoomService roomService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private ControlService controlService;

    private ApiClient client;
    @SuppressWarnings("SpellCheckingInspection")
    private Gson gsonRoom;
    @SuppressWarnings("SpellCheckingInspection")
    private Gson gsonEquipment;
    @SuppressWarnings("SpellCheckingInspection")
    private Gson gsonControl;
    @SuppressWarnings("SpellCheckingInspection")
    private Gson gsonValue;

    private static final String PORT = "8080";
    private static final String TEST_DATASOURCE = "jdbc:h2:mem:testing";

    private Room testingRoom = new Room("Testing room name", 152);
    private Equipment testingEquipment = new Equipment("Testing equipment name");
    private Control testingControl = new Control("Testing Temperature");


    @SuppressWarnings("AccessStaticViaInstance")
    @BeforeClass
    public static void startServer() {
        System.setProperty("properties.home", "/Users/alexeypapin/IdeaProjects/HomeAutomation/propertiesTest/");
        String[] args = {PORT, TEST_DATASOURCE};
        Application.main(args);
    }

    @AfterClass
    public static void stopServer() {
        // SpringApplication.exit(Application.getAppContext(), (ExitCodeGenerator) () -> 0);
    }

    @Before public void setUp() throws Exception {
        client = new ApiClient("http://localhost:8080");
        gsonRoom = new GsonBuilder()
            .registerTypeAdapter(Room.class, new Room.RoomSerializer())
            .registerTypeAdapter(Room.class, new Room.RoomDeserializer())
            .registerTypeAdapter(List.class, new Room.ListRoomSerializer())
            .registerTypeAdapter(List.class, new Room.ListRoomDeserializer())
            .create();
        gsonEquipment = new GsonBuilder()
            .registerTypeAdapter(Equipment.class, new Equipment.EquipmentSerializer())
            .registerTypeAdapter(Equipment.class, new Equipment.EquipmentDeserializer())
            .registerTypeAdapter(List.class, new Equipment.ListEquipmentSerializer())
            .registerTypeAdapter(List.class, new Equipment.ListEquipmentDeserializer())
            .create();
        gsonControl = new GsonBuilder()
            .registerTypeAdapter(Control.class, new Control.ControlSerializer())
            .registerTypeAdapter(Control.class, new Control.ControlDeserializer())
            .registerTypeAdapter(List.class, new Control.ListControlSerializer())
            .registerTypeAdapter(List.class, new Control.ListControlDeserializer())
            .create();
        gsonValue = new GsonBuilder()
            .registerTypeAdapter(Control.class, new Control.ControlValueSerializer())
            .registerTypeAdapter(BigDecimal.class, new Control.ControlValueDeserializer())
            .create();

        roomService.save(testingRoom);
        equipmentService.save(testingEquipment);
        controlService.save(testingControl);

        testingRoom.addEquipment(testingEquipment);
        testingEquipment.setRoom(testingRoom);
        testingEquipment.addControl(testingControl);
        testingControl.setEquipment(testingEquipment);

        roomService.save(testingRoom);
        equipmentService.save(testingEquipment);
        controlService.save(testingControl);
    }

    @After public void tearDown() throws Exception {
        clearAll();
    }

    // ----------------------------------- RoomController -----------------------------------------------
    
    @Test public void testAddRoom() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("roomName", "Kitchen");
        values.put("squareFootage", "325");

        ApiResponse res = client.request("POST", "/room", gsonRoom.toJson(values));

        assertEquals(HttpStatus.OK.value(), res.getStatus());
    }

    @SuppressWarnings("unchecked")
    @Test public void testGetAllRooms() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("roomName", "Kitchen");
        values.put("squareFootage", "325");

        ApiResponse res = client.request("POST", "/room", gsonRoom.toJson(values));
        assertEquals(HttpStatus.OK.value(), res.getStatus());

        values.clear();
        values.put("roomName", "Hall");
        values.put("squareFootage", "76");

        res = client.request("POST", "/room", gsonRoom.toJson(values));
        assertEquals(HttpStatus.OK.value(), res.getStatus());

        values.clear();
        values.put("roomName", "Garden");
        values.put("squareFootage", "99");

        res = client.request("POST", "/room", gsonRoom.toJson(values));
        assertEquals(HttpStatus.OK.value(), res.getStatus());

        res = client.request("GET", "/room");

        assertEquals(HttpStatus.OK.value(), res.getStatus());

        List<Room> rooms = gsonRoom.fromJson(res.getBody(), List.class);

        assertEquals(4, rooms.size());
        assertEquals(testingRoom.getName(), rooms.get(0).getName());
        assertEquals("Kitchen", rooms.get(1).getName());
        assertEquals("Hall", rooms.get(2).getName());
        assertEquals("Garden", rooms.get(3).getName());
    }

    @SuppressWarnings("unchecked")
    @Test public void testUpdateRoom() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("roomName", "Garden");
        values.put("squareFootage", "76");
        ApiResponse res = client.request("PUT", "/room/"+testingRoom.getId(), gsonRoom.toJson(values));
        assertEquals(HttpStatus.OK.value(), res.getStatus());

        res = client.request("GET", "/room/"+testingRoom.getId());
        Room updatedRoom = gsonRoom.fromJson(res.getBody(), Room.class);
        assertEquals(1, roomService.findAll().size());
        assertEquals("Garden", updatedRoom.getName());
        assertEquals(76, updatedRoom.getSquareFootage());
    }

    @Test public void testGetRoomById() throws Exception {
        Room room1 = new Room("Kitchen", 325);
        Room room2 = new Room("Hall", 76);
        Room room3 = new Room("Garden", 99);
        Long id1 = roomService.save(room1);
        Long id2 = roomService.save(room2);
        Long id3 = roomService.save(room3);

        ApiResponse res = client.request("GET", "/room/" + id1);
        assertEquals(HttpStatus.OK.value(), res.getStatus());
        Room retrievedRoom = gsonRoom.fromJson(res.getBody(), Room.class);
        assertEquals(room1, retrievedRoom);

        res = client.request("GET", "/room/" + id2);
        assertEquals(HttpStatus.OK.value(), res.getStatus());
        retrievedRoom = gsonRoom.fromJson(res.getBody(), Room.class);
        assertEquals(room2, retrievedRoom);

        res = client.request("GET", "/room/" + id3);
        assertEquals(HttpStatus.OK.value(), res.getStatus());
        retrievedRoom = gsonRoom.fromJson(res.getBody(), Room.class);
        assertEquals(room3, retrievedRoom);
    }

    @Test public void testDeleteRoomById() throws Exception {
        ApiResponse res = client.request("DELETE", "/room/" + testingRoom.getId());
        assertEquals(HttpStatus.NO_CONTENT.value(), res.getStatus());

        assertEquals(0, roomService.findAll().size());
    }

    // ----------------------------------- EquipmentController -----------------------------------------------

    @Test
    public void testAddEquipment() throws Exception {
        assertEquals(1, equipmentService.findAll().size());
        Map<String, Object> values = new HashMap<>();
        values.put("equipmentName", "Kitchen");

        ApiResponse res = client.request("POST", "/room/"+testingRoom.getId()+"/equipment", gsonEquipment.toJson(values));

        assertEquals(HttpStatus.OK.value(), res.getStatus());
        assertEquals(2, equipmentService.findAll().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllEquipmentByRoom() throws Exception {
        Room room = new Room("Another room", 99);
        roomService.save(room);
        Equipment equipment1 = new Equipment("Thermostat");
        equipment1.setRoom(testingRoom);
        equipmentService.save(equipment1);
        Equipment equipment2 = new Equipment("Fan");
        equipment2.setRoom(testingRoom);
        equipmentService.save(equipment2);
        Equipment equipment3 = new Equipment("Fan");
        equipment3.setRoom(room);
        equipmentService.save(equipment3);

        ApiResponse res = client.request("GET", "/room/"+testingRoom.getId()+"/equipment");
        List<Equipment> equipments = gsonEquipment.fromJson(res.getBody(), List.class);

        assertEquals(HttpStatus.OK.value(), res.getStatus());
        assertEquals(3, equipments.size());
    }

    @Test
    public void testUpdateEquipment() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("equipmentName", "Thermostat");

        ApiResponse res = client.request("PUT", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId(), gsonEquipment.toJson(values));
        assertEquals(HttpStatus.OK.value(), res.getStatus());

        Equipment equipment = equipmentService.findById(testingEquipment.getId());
        assertEquals(1, equipmentService.findAll().size());
        assertEquals("Thermostat", equipment.getName());
    }

    @Test
    public void testGetEquipmentById() throws Exception {
        ApiResponse res = client.request("GET", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId());
        assertEquals(HttpStatus.OK.value(), res.getStatus());
        Equipment found = gsonEquipment.fromJson(res.getBody(), Equipment.class);

        found.setRoom(testingEquipment.getRoom());
        assertEquals(found, testingEquipment);
    }

    @Test
    public void testDeleteEquipmentById() throws Exception {
        ApiResponse res = client.request("DELETE", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId());
        assertEquals(HttpStatus.NO_CONTENT.value(), res.getStatus());

        assertEquals(0, equipmentService.findAll().size());
    }

    // ----------------------------------- ControlController -----------------------------------------------

    @Test
    public void testAddControl() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("controlName", "cooling Temp");

        ApiResponse res = client.request("POST", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId()+"/control", gsonControl.toJson(values));

        assertEquals(HttpStatus.OK.value(), res.getStatus());
        assertEquals(2, controlService.findAll().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllControlsByEquipment() throws Exception {
        Equipment equipment = new Equipment("Fan");
        equipmentService.save(equipment);

        Control control1 = new Control("low Fan cicles");
        control1.setEquipment(testingEquipment);
        controlService.save(control1);
        Control control2 = new Control("high Fan cicles");
        control2.setEquipment(testingEquipment);
        controlService.save(control2);
        Control control3 = new Control("Other control");
        control3.setEquipment(equipment);
        controlService.save(control3);

        ApiResponse res = client.request("GET", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId()+"/control");
        List<Control> controls = gsonControl.fromJson(res.getBody(), List.class);

        assertEquals(HttpStatus.OK.value(), res.getStatus());
        assertEquals(3, controls.size());
    }

    @Test
    public void testUpdateControl() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("controlName", "Switch");

        ApiResponse res = client.request("PUT", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId()+"/control/"+testingControl.getId(), gsonControl.toJson(values));
        assertEquals(HttpStatus.OK.value(), res.getStatus());

        Control control = controlService.findById(testingControl.getId());
        assertEquals(1, controlService.findAll().size());
        assertEquals("Switch", control.getName());
    }

    @Test
    public void testGetControlById() throws Exception {
        ApiResponse res = client.request("GET", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId()+"/control/"+testingControl.getId());
        assertEquals(HttpStatus.OK.value(), res.getStatus());
        Control found = gsonControl.fromJson(res.getBody(), Control.class);
        found.setEquipment(testingControl.getEquipment());
        found.setValue(testingControl.getValue());

        assertEquals(found, testingControl);
    }

    @Test
    public void testDeleteControlById() throws Exception {
        ApiResponse res = client.request("DELETE", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId()+"/control/"+testingControl.getId());
        assertEquals(HttpStatus.NO_CONTENT.value(), res.getStatus());

        assertEquals(0, controlService.findAll().size());
    }

    // ----------------------------------- Value (ControlController) -----------------------------------------------
    // Decimal digits after point - 2 by default

    @Test
    public void testAddValue() throws Exception {
        String value = "99.45";
        ApiResponse res = client.request("POST", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId()+"/control/"+testingControl.getId()+"/value", gsonValue.toJson(value));

        assertEquals(HttpStatus.OK.value(), res.getStatus());
        BigDecimal retrieved= BigDecimal.valueOf(controlService.findById(testingControl.getId()).getValue().doubleValue());

        assertEquals(BigDecimal.valueOf(Double.parseDouble(value)), retrieved);
    }

    @Test
    public void testGetValue() throws Exception {
        BigDecimal value = BigDecimal.valueOf(176.87);
        testingControl.setValue(value);
        controlService.save(testingControl);
        ApiResponse res = client.request("GET", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId()+"/control/"+testingControl.getId()+"/value");
        assertEquals(HttpStatus.OK.value(), res.getStatus());
        BigDecimal retrieved= BigDecimal.valueOf(controlService.findById(testingControl.getId()).getValue().doubleValue());

        assertEquals(value, retrieved);
    }

    private Gson gson = new GsonBuilder()
        .registerTypeAdapter(ApiError.class, new ApiError.ApiErrorSerializer())
        .registerTypeAdapter(ApiError.class, new ApiError.ApiErrorDeserializer())
        .setPrettyPrinting()
        .create();

    // ----------------------------------- Exceptions -----------------------------------------------

    @Test
    public void testGetRoomWithBadIdThrowsNotFoundException() throws Exception {
        ApiResponse res = client.request("GET", "/room/9999");
        assertNotFoundMessage(res);
    }

    @Test
    public void testGetEquipmentWithBadIdThrowsNotFoundException() throws Exception {
        ApiResponse res = client.request("GET", "/room/"+testingRoom.getId()+"/equipment/9999");
        assertNotFoundMessage(res);
    }

    @Test
    public void testGetControlWithBadIdThrowsNotFoundException() throws Exception {
        ApiResponse res = client.request("GET", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId()+"/control/9999");
        assertNotFoundMessage(res);
    }

    @Test
    public void testGetValueWithBadIdThrowsNotFoundException() throws Exception {
        ApiResponse res = client.request("GET", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId()+"/control/9999/value");
        assertNotFoundMessage(res);
    }

    @Test
    public void testAddRoomWithBadDataThrowsBadRequestException() throws Exception {
        Map<String, String> value = new HashMap<>();
        value.put("badRequest", "because must be 2 pairs key-value in map");
        ApiResponse res = client.request("POST", "/room", gsonRoom.toJson(value));
        assertBadRequestMessage(res);
    }

    @Test
    public void testUpdateRoomWithBadDataThrowsBadRequestException() throws Exception {
        Map<String, String> value = new HashMap<>();
        value.put("badRequest", "because must be 2 pairs key-value in map");
        ApiResponse res = client.request("PUT", "/room/"+testingRoom.getId(), gsonRoom.toJson(value));
        assertBadRequestMessage(res);
    }

    @Test
    public void testUpdateRoomWithoutIdThrowsBadRequestException() throws Exception {
        Map<String, String> value = new HashMap<>();
        value.put("badRequest", "because must be 2 pairs key-value in map");
        ApiResponse res = client.request("PUT", "/room", gsonRoom.toJson(value));
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), res.getStatus());
    }

    @Test
    public void testAddEquipmentWithBadDataThrowsBadRequestException() throws Exception {
        Map<String, String> value = new HashMap<>();
        value.put("badRequest", "because must be equipmentName key");
        ApiResponse res = client.request("POST", "/room/"+testingRoom.getId()+"/equipment", gsonRoom.toJson(value));
        assertBadRequestMessage(res);
    }

    @Test
    public void testUpdateEquipmentWithBadDataThrowsBadRequestException() throws Exception {
        Map<String, String> value = new HashMap<>();
        value.put("badRequest", "because must be equipmentName key");
        ApiResponse res = client.request("PUT", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId(), gsonRoom.toJson(value));
        assertBadRequestMessage(res);
    }

    @Test
    public void testAddControlWithBadDataThrowsBadRequestException() throws Exception {
        Map<String, String> value = new HashMap<>();
        value.put("badRequest", "because must be controlName key");
        ApiResponse res = client.request("POST", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId()+"/control", gsonRoom.toJson(value));
        assertBadRequestMessage(res);
    }

    @Test
    public void testUpdateControlWithBadDataThrowsBadRequestException() throws Exception {
        Map<String, String> value = new HashMap<>();
        value.put("badRequest", "because must be controlName key");
        ApiResponse res = client.request("PUT", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId()+"/control/"+testingControl.getId(), gsonRoom.toJson(value));
        assertBadRequestMessage(res);
    }

    @Test
    public void testDeleteWithBadIdThrowsNotFoundException() throws Exception {
        ApiResponse res = client.request("DELETE", "/room/9999");
        assertNotFoundMessage(res);
        res = client.request("DELETE", "/room/"+testingRoom.getId()+"/equipment/9999");
        assertNotFoundMessage(res);
        res = client.request("DELETE", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId()+"/control/9999");
        assertNotFoundMessage(res);
    }

    // ----------------------------------- GetAll (Room Controller -----------------------------------------------

    @SuppressWarnings("unchecked") @Test
    public void testGetAllTree() throws Exception {
        final int ROOMS_COUNT = 5, EQUIPMENTS_COUNT = 5, CONTROL_COUNT = 5;
        final BigDecimal lostValue = BigDecimal.valueOf(76543.86);
        final int r = 3; // where will be lostValue, roomNumber, equipNumber, controlNumber
        final int e = 1; // where will be lostValue, roomNumber, equipNumber, controlNumber
        final int c = 4; // where will be lostValue, roomNumber, equipNumber, controlNumber

        clearAll();
        List <Control> controls = new ArrayList<>();
        List<Equipment> equipments = new ArrayList<>();
        List<Room> rooms = new ArrayList<>();

        for (int i=0;i<CONTROL_COUNT*EQUIPMENTS_COUNT*ROOMS_COUNT;i++) {
            controls.add(new Control("Test name control#"+i));
            controlService.save(controls.get(i));
        }

        for (int i=0;i<EQUIPMENTS_COUNT*ROOMS_COUNT;i++) {
            equipments.add(new Equipment("Test name equipment#"+i));
            equipmentService.save(equipments.get(i));
        }

        for (int i=0;i<ROOMS_COUNT;i++) {
            rooms.add(new Room("Test name room#"+i,  (int) ((i+1)*32.8)));
            roomService.save(rooms.get(i));
        }

        int indexRoom = 0;
        int indexEquipment = 0;
        int indexControl = 0;

        for (int i=0;i<ROOMS_COUNT;i++) {
            for (int j=0;j<EQUIPMENTS_COUNT;j++) {
                rooms.get(indexRoom).addEquipment(equipments.get(indexEquipment));
                equipments.get(indexEquipment).setRoom(rooms.get(indexRoom));
                equipmentService.save(equipments.get(indexEquipment));
                roomService.save(rooms.get(indexRoom));
                for (int k=0;k<CONTROL_COUNT;k++) {
                    equipments.get(indexEquipment).addControl(controls.get(indexControl));
                    controls.get(indexControl).setEquipment(equipments.get(indexEquipment));
                    controlService.save(controls.get(indexControl));
                    equipmentService.save(equipments.get(indexEquipment));
                    if ((i==r)&&(j==e)&&(k==c)) {
                        controls.get(indexControl).setValue(lostValue);
                    }
                    indexControl++;
                }
                indexEquipment++;
            }
            indexRoom++;
        }

        assertEquals(lostValue, rooms.get(r).getEquipments().get(e).getControls().get(c).getValue());
    }

    private static final ApiError ERROR_NOT_FOUND = new ApiError(404, "");
    private static final ApiError ERROR_BAD_REQUEST = new ApiError(400,"");

    @SuppressWarnings("unchecked")
    private void assertNotFoundMessage (ApiResponse res) {
        ApiError error = gson.fromJson(res.getBody(), ApiError.class);
        assertEquals(ERROR_NOT_FOUND.getStatus(), error.getStatus());

        Map<String, String> errorMsg = gson.fromJson(res.getBody(), Map.class);

        assertEquals(HttpStatus.NOT_FOUND.value(), res.getStatus());
        assertEquals("com.zzheads.HomeAutomation.exceptions.ApiErrorNotFound", errorMsg.get("exception"));
        assertEquals("Not Found", errorMsg.get("error"));
        assertTrue(errorMsg.containsKey("path"));
        assertTrue(errorMsg.containsKey("timestamp"));
        assertTrue(errorMsg.containsKey("message"));
        assertTrue(errorMsg.containsKey("status"));
    }

    @SuppressWarnings("unchecked")
    private void assertBadRequestMessage (ApiResponse res) {
        ApiError error = gson.fromJson(res.getBody(), ApiError.class);
        assertEquals(ERROR_BAD_REQUEST.getStatus(), error.getStatus());

        Map<String, String> errorMsg = gson.fromJson(res.getBody(), Map.class);

        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getStatus());
        assertEquals("com.zzheads.HomeAutomation.exceptions.ApiErrorBadRequest", errorMsg.get("exception"));
        assertEquals("Bad Request", errorMsg.get("error"));
        assertTrue(errorMsg.containsKey("path"));
        assertTrue(errorMsg.containsKey("timestamp"));
        assertTrue(errorMsg.containsKey("message"));
        assertTrue(errorMsg.containsKey("status"));
    }

    private void clearAll() throws DaoException {
        for (Control c : controlService.findAll()) controlService.delete(c);
        for (Equipment e : equipmentService.findAll()) equipmentService.delete(e);
        for (Room r : roomService.findAll()) roomService.delete(r);
    }
}
