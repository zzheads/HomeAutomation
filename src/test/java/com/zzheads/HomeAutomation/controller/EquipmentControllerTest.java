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
public class EquipmentControllerTest {
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

    private Room testingRoom = new Room("Testing room name", 152);
    private Equipment testingEquipment = new Equipment("Testing equipment name");

    @SuppressWarnings("AccessStaticViaInstance")
    @BeforeClass
    public static void startServer() {
        String[] args = {PORT, TEST_DATASOURCE};
        Application.main(args);
    }

    @AfterClass
    public static void stopServer() {
        SpringApplication.exit(Application.getAppContext(), (ExitCodeGenerator) () -> 0);
    }

    @Before public void setUp() throws Exception {
        client = new ApiClient("http://localhost:8080");
        gson = new GsonBuilder()
            .registerTypeAdapter(Equipment.class, new Equipment.EquipmentSerializer())
            .registerTypeAdapter(Equipment.class, new Equipment.EquipmentDeserializer())
            .registerTypeAdapter(List.class, new Equipment.ListEquipmentDeserializer())
            .setPrettyPrinting()
            .create();

        roomService.save(testingRoom);
        equipmentService.save(testingEquipment);
        testingRoom.addEquipment(testingEquipment);
        testingEquipment.setRoom(testingRoom);
        roomService.save(testingRoom);
        equipmentService.save(testingEquipment);
    }

    @After public void tearDown() throws Exception {
        clearAll();
    }

    @Test
    public void testAddEquipment() throws Exception {
        assertEquals(1, equipmentService.findAll().size());
        Map<String, Object> values = new HashMap<>();
        values.put("equipmentName", "Kitchen");

        ApiResponse res = client.request("POST", "/room/"+testingRoom.getId()+"/equipment", gson.toJson(values));

        assertEquals(HttpStatus.CREATED.value(), res.getStatus());
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
        List<Equipment> equipments = gson.fromJson(res.getBody(), List.class);

        assertEquals(HttpStatus.OK.value(), res.getStatus());
        assertEquals(3, equipments.size());
    }

    @Test
    public void testUpdateEquipment() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("equipmentName", "Thermostat");

        ApiResponse res = client.request("PUT", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId(), gson.toJson(values));
        assertEquals(HttpStatus.CREATED.value(), res.getStatus());

        Equipment equipment = equipmentService.findById(testingEquipment.getId());
        assertEquals(1, equipmentService.findAll().size());
        assertEquals("Thermostat", equipment.getName());
    }

    @Test
    public void testGetEquipmentById() throws Exception {
        ApiResponse res = client.request("GET", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId());
        assertEquals(HttpStatus.OK.value(), res.getStatus());
        Equipment found = gson.fromJson(res.getBody(), Equipment.class);

        assertEquals(found, testingEquipment);
    }

    @Test
    public void testDeleteEquipmentById() throws Exception {
        ApiResponse res = client.request("DELETE", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId());
        assertEquals(HttpStatus.NO_CONTENT.value(), res.getStatus());

        assertEquals(0, equipmentService.findAll().size());
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
