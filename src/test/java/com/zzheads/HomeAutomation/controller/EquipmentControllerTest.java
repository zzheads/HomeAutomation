package com.zzheads.HomeAutomation.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzheads.HomeAutomation.Application;
import com.zzheads.HomeAutomation.model.Equipment;
import com.zzheads.HomeAutomation.model.Room;
import com.zzheads.HomeAutomation.service.ControlService;
import com.zzheads.HomeAutomation.service.EquipmentService;
import com.zzheads.HomeAutomation.service.RoomService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class EquipmentControllerTest {

    final String BASE_URL = "http://localhost:8080/";

    @Mock private MockHttpSession session;

    @Mock private RoomService roomService;
    @Mock private EquipmentService equipmentService;
    @Mock private ControlService controlService;
    @InjectMocks private EquipmentController equipmentController;

    private MockMvc mockMvc;
    private Gson gson = new GsonBuilder()
        .registerTypeAdapter(Equipment.class, new Equipment.EquipmentSerializer())
        .registerTypeAdapter(Equipment.class, new Equipment.EquipmentDeserializer())
        .registerTypeAdapter(List.class, new Equipment.ListEquipmentSerializer())
        .registerTypeAdapter(List.class, new Equipment.ListEquipmentDeserializer())
        .create();

    static {
        System.setProperty("properties.home", "D:/Projects/HomeAutomation/propertiesTest/");
    }

    @Before public void setup() {
        MockitoAnnotations.initMocks(this);
        //roomController = new RoomController();
        mockMvc = MockMvcBuilders.standaloneSetup(equipmentController).build();
    }

    @Test public void addEquipmentTest() throws Exception {
        Room room = new Room();
        Map<String, String> req = new HashMap<>();
        req.put("equipmentName", "Cooler");
        Equipment equipment = new Equipment(req);

        when(roomService.findById(1L)).thenReturn(room);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/room/1/equipment").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(req)))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        Equipment retrieved = gson.fromJson(result.getResponse().getContentAsString(), Equipment.class);
        verify(equipmentService).save(Matchers.any(Equipment.class));
        assertEquals(equipment, retrieved);
    }

    @SuppressWarnings("unchecked")
    @Test public void getAllEquipmentTest() throws Exception {
        Room room = new Room();
        Equipment equipment1 = new Equipment(1L, "Cooler");
        equipment1.setRoom(room);
        Equipment equipment2 = new Equipment(1L, "Refrigerator");
        equipment2.setRoom(room);
        List<Equipment> equipments = new ArrayList<>();
        equipments.add(equipment1);
        equipments.add(equipment2);

        when(equipmentService.findByRoom(1L)).thenReturn(equipments);
        when(roomService.findById(1L)).thenReturn(room);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/room/1/equipment").contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        List<Equipment> retrieved = gson.fromJson(result.getResponse().getContentAsString(), List.class);
        Equipment e1 = retrieved.get(0);
        Equipment e2 = retrieved.get(1);
        verify(equipmentService).findByRoom(1L);
        retrieved.get(0).setRoom(equipments.get(0).getRoom());
        retrieved.get(1).setRoom(equipments.get(1).getRoom());
        assertEquals(equipments, retrieved);
    }

    @Test public void updateEquipmentTest() throws Exception {
        Room room = new Room(1L, "Hall", 245);
        Equipment equipment = new Equipment(1L, "Cooler");
        equipment.setRoom(room);
        when(roomService.findById(1L)).thenReturn(room);
        when(equipmentService.findById(1L)).thenReturn(equipment);

        Map<String, String> req = new HashMap<>();
        req.put("equipmentName", "Thermostat");
        Equipment updatedEquipment = new Equipment(req);
        updatedEquipment.setId(1L);
        updatedEquipment.setRoom(room);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/room/1/equipment/1").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(req)))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        Equipment retrieved = gson.fromJson(result.getResponse().getContentAsString(), Equipment.class);
        retrieved.setRoom(room);

        assertEquals(updatedEquipment, retrieved);
        verify(equipmentService).save(updatedEquipment);
    }

    @Test public void getEquipmentByIdTest() throws Exception {
        Room room = new Room(1L, "Hall", 245);
        Equipment equipment = new Equipment(1L, "Cooler");
        equipment.setRoom(room);
        when(roomService.findById(1L)).thenReturn(room);
        when(equipmentService.findById(1L)).thenReturn(equipment);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/room/1/equipment/1").contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        Equipment retrieved = gson.fromJson(result.getResponse().getContentAsString(), Equipment.class);
        retrieved.setRoom(room);

        verify(equipmentService).findById(1L);
        assertEquals(equipment, retrieved);
    }

    @Test public void deleteEquipmentByIdTest() throws Exception {
        Room room = new Room(1L, "Hall", 245);
        Equipment equipment = new Equipment(3L, "Cooler");
        equipment.setRoom(room);
        when(roomService.findById(1L)).thenReturn(room);
        when(equipmentService.findById(3L)).thenReturn(equipment);

        MvcResult result =mockMvc.perform(MockMvcRequestBuilders.delete("/room/1/equipment/3").contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();

        verify(equipmentService).findById(3L);
        verify(equipmentService).delete(Matchers.any(Equipment.class));
    }

    // _________________ Some tests for exceptions _________________

    @Test
    public void addEquipmentWithNoDataThrowsBadRequestExceptionTest() throws Exception {
        Map<String, String> req = new HashMap<>();
        req.put("badData", "Kitchen");

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/room/1/equipment").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(req)));
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ApiErrorBadRequest: Can't make that request. Expected data format: {\"equipmentName\" : equipmentName}"));
        }
    }

    @Test
    public void findEquipmentWithBadIdThrowsNotFoundExceptionTest() throws Exception {
        when(roomService.findById(1L)).thenReturn(new Room());

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/room/1/equipment/9999").contentType(MediaType.APPLICATION_JSON));
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ApiErrorNotFound: Can't find equipment with 9999 id."));
        }
    }

    @Test
    public void updateRoomWithNoDataThrowsBadRequestExceptionTest() throws Exception {
        Map<String, String> req = new HashMap<>();
        req.put("equipmentName", "New name of equipment");
        Map<String, String> badReq = new HashMap<>();
        badReq.put("badData", "Data bad!");
        when(roomService.findById(1L)).thenReturn(new Room());
        when(equipmentService.findById(1L)).thenReturn(new Equipment());

        try {
            mockMvc.perform(MockMvcRequestBuilders.put("/room/1/equipment/1").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(badReq)));
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ApiErrorBadRequest: Can't make that request. Expected data format: {\"equipmentName\" : equipmentName}"));
        }

        try {
            mockMvc.perform(MockMvcRequestBuilders.put("/room/1/equipment/9999").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(req)));
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ApiErrorNotFound: Can't find equipment with 9999 id."));
        }
    }

    @Test
    public void deleteRoomWithBadIdThrowsNotFoundExceptionTest() throws Exception {
        when(roomService.findById(1L)).thenReturn(new Room());
        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/room/1/equipment/9999").contentType(MediaType.APPLICATION_JSON));
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ApiErrorNotFound: Can't find equipment with 9999 id."));
        }
    }
}
