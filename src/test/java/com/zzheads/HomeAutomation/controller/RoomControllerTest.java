package com.zzheads.HomeAutomation.controller;

import com.google.gson.*;
import com.zzheads.HomeAutomation.Application;
import com.zzheads.HomeAutomation.config.DataConfig;
import com.zzheads.HomeAutomation.exceptions.ApiErrorBadRequest;
import com.zzheads.HomeAutomation.exceptions.DaoException;
import com.zzheads.HomeAutomation.model.Control;
import com.zzheads.HomeAutomation.model.Equipment;
import com.zzheads.HomeAutomation.model.Room;
import com.zzheads.HomeAutomation.service.ControlService;
import com.zzheads.HomeAutomation.service.EquipmentService;
import com.zzheads.HomeAutomation.service.RoomService;
import com.zzheads.HomeAutomation.testing.ApiClient;
import com.zzheads.HomeAutomation.testing.ApiResponse;
import org.eclipse.jetty.io.Connection;
import org.hibernate.SessionFactory;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import javax.swing.*;
import javax.validation.constraints.AssertTrue;

import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.*;

//
// HomeAutomation
// com.zzheads.HomeAutomation.controller created by zzheads on 22.08.2016.
//
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class RoomControllerTest {
    private static Application app;
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private RoomService roomService;
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private EquipmentService equipmentService;
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private ControlService controlService;


    private ApiClient client;
    private Gson gson;
    private static final String PORT = "4568";
    private static final String TEST_DATASOURCE = "jdbc:h2:mem:testing";

    static {
        System.setProperty("properties.home", "E:/Projects/HomeAutomation/propertiesTest/");
    }

    @SuppressWarnings("AccessStaticViaInstance")
    @BeforeClass
    public static void startServer() {
        String[] args = {PORT, TEST_DATASOURCE};
        app = new Application();
        app.main(args);
    }

    @AfterClass
    public static void stopServer() {

    }


    @Before public void setUp() throws Exception {
        client = new ApiClient("http://localhost:8080");
        gson = new GsonBuilder()
            .registerTypeAdapter(Room.class, new Room.RoomDeserializer())
            .registerTypeAdapter(Room.class, new Room.RoomSerializer())
            .registerTypeAdapter(List.class, new Room.ListRoomDeserializer())
            .setPrettyPrinting()
            .create();
        clearAll();
    }

    @After public void tearDown() throws Exception {

    }

    @Test public void testAddRoom() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("roomName", "Kitchen");
        values.put("squareFootage", "325");

        ApiResponse res = client.request("POST", "/room", gson.toJson(values));

        assertEquals(HttpStatus.CREATED.value(), res.getStatus());
    }

    @SuppressWarnings("unchecked")
    @Test public void testGetAllRooms() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("roomName", "Kitchen");
        values.put("squareFootage", "325");

        ApiResponse res = client.request("POST", "/room", gson.toJson(values));
        assertEquals(HttpStatus.CREATED.value(), res.getStatus());

        values.clear();
        values.put("roomName", "Hall");
        values.put("squareFootage", "76");

        res = client.request("POST", "/room", gson.toJson(values));
        assertEquals(HttpStatus.CREATED.value(), res.getStatus());

        values.clear();
        values.put("roomName", "Garden");
        values.put("squareFootage", "99");

        res = client.request("POST", "/room", gson.toJson(values));
        assertEquals(HttpStatus.CREATED.value(), res.getStatus());

        res = client.request("GET", "/room");

        assertEquals(HttpStatus.OK.value(), res.getStatus());

        List<Room> rooms = gson.fromJson(res.getBody(), List.class);

        assertEquals(rooms.size(), 3);
        assertEquals(rooms.get(0).getName(), "Kitchen");
        assertEquals(rooms.get(1).getName(), "Hall");
        assertEquals(rooms.get(2).getName(), "Garden");
    }

    @SuppressWarnings("unchecked")
    @Test public void testUpdateRoom() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("roomName", "Kitchen");
        values.put("squareFootage", "325");
        ApiResponse res = client.request("POST", "/room", gson.toJson(values));
        assertEquals(HttpStatus.CREATED.value(), res.getStatus());

        res = client.request("GET", "/room");
        assertEquals(HttpStatus.OK.value(), res.getStatus());
        List<Room> rooms = gson.fromJson(res.getBody(), List.class);
        assertEquals(rooms.size(), 1);
        Long id = rooms.get(0).getId();

        rooms.clear();
        values.clear();
        values.put("roomName", "Garden");
        values.put("squareFootage", "76");
        res = client.request("PUT", "/room/"+id, gson.toJson(values));
        assertEquals(HttpStatus.CREATED.value(), res.getStatus());

        res = client.request("GET", "/room");
        rooms = gson.fromJson(res.getBody(), List.class);
        assertEquals(rooms.size(), 1);
        assertEquals(rooms.get(0).getName(), "Garden");
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
        Room retrievedRoom = gson.fromJson(res.getBody(), Room.class);
        assertEquals(room1, retrievedRoom);

        res = client.request("GET", "/room/" + id2);
        assertEquals(HttpStatus.OK.value(), res.getStatus());
        retrievedRoom = gson.fromJson(res.getBody(), Room.class);
        assertEquals(room2, retrievedRoom);

        res = client.request("GET", "/room/" + id3);
        assertEquals(HttpStatus.OK.value(), res.getStatus());
        retrievedRoom = gson.fromJson(res.getBody(), Room.class);
        assertEquals(room3, retrievedRoom);
    }

    @Test public void testDeleteRoomById() throws Exception {
        Room room1 = new Room("Kitchen", 325);
        Room room2 = new Room("Hall", 76);
        Room room3 = new Room("Garden", 99);
        Long id1 = roomService.save(room1);
        Long id2 = roomService.save(room2);
        Long id3 = roomService.save(room3);

        ApiResponse res = client.request("DELETE", "/room/" + id1);
        assertEquals(HttpStatus.NO_CONTENT.value(), res.getStatus());

        List<Room> rooms = roomService.findAll();
        assertEquals(rooms.size(), 2);
        assertTrue(!Objects.equals(rooms.get(0).getId(), id1) && !Objects.equals(rooms.get(1).getId(), id1));
    }


    private void clearAll() throws DaoException {
        for (Control c : controlService.findAll())
            controlService.delete(c);
        for (Equipment e : equipmentService.findAll())
            equipmentService.delete(e);
        for (Room r : roomService.findAll())
            roomService.delete(r);
    }

}
