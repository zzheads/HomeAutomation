package com.zzheads.HomeAutomation.controller;

import com.google.gson.*;
import com.zzheads.HomeAutomation.Application;
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
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.*;

//
// HomeAutomation
// com.zzheads.HomeAutomation.controller created by zzheads on 22.08.2016.
//
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ControlControllerTest {
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
    private Control testingControl = new Control("Testing Temperature");

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
            .registerTypeAdapter(Control.class, new Control.ControlSerializer())
            .registerTypeAdapter(Control.class, new Control.ControlDeserializer())
            .registerTypeAdapter(List.class, new Control.ListControlDeserializer())
            .setPrettyPrinting()
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

    @Test
    public void testAddControl() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("controlName", "cooling Temp");

        ApiResponse res = client.request("POST", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId()+"/control", gson.toJson(values));

        assertEquals(HttpStatus.CREATED.value(), res.getStatus());
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
        List<Control> controls = gson.fromJson(res.getBody(), List.class);

        assertEquals(HttpStatus.OK.value(), res.getStatus());
        assertEquals(3, controls.size());
    }

    @Test
    public void testUpdateControl() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("controlName", "Switch");

        ApiResponse res = client.request("PUT", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId()+"/control/"+testingControl.getId(), gson.toJson(values));
        assertEquals(HttpStatus.CREATED.value(), res.getStatus());

        Control control = controlService.findById(testingControl.getId());
        assertEquals(1, controlService.findAll().size());
        assertEquals("Switch", control.getName());
    }

    @Test
    public void testGetControlById() throws Exception {
        ApiResponse res = client.request("GET", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId()+"/control/"+testingControl.getId());
        assertEquals(HttpStatus.OK.value(), res.getStatus());
        Control found = gson.fromJson(res.getBody(), Control.class);

        assertEquals(found, testingControl);
    }

    @Test
    public void testDeleteControlById() throws Exception {
        ApiResponse res = client.request("DELETE", "/room/"+testingRoom.getId()+"/equipment/"+testingEquipment.getId()+"/control/"+testingControl.getId());
        assertEquals(HttpStatus.NO_CONTENT.value(), res.getStatus());

        assertEquals(0, controlService.findAll().size());
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
