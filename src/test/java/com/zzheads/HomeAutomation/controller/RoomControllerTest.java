package com.zzheads.HomeAutomation.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzheads.HomeAutomation.Application;
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
public class RoomControllerTest {

    final String BASE_URL = "http://localhost:8080/";

    @Mock private MockHttpSession session;

    @Mock private RoomService roomService;
    @Mock private EquipmentService equipmentService;
    @Mock private ControlService controlService;
    @InjectMocks private RoomController roomController;

    private MockMvc mockMvc;
    private Gson gson = new GsonBuilder()
        .registerTypeAdapter(Room.class, new Room.RoomSerializer())
        .registerTypeAdapter(Room.class, new Room.RoomDeserializer())
        .registerTypeAdapter(List.class, new Room.ListRoomSerializer())
        .registerTypeAdapter(List.class, new Room.ListRoomDeserializer())
        .create();
    private Gson gsonExc = new Gson();


    static {
        System.setProperty("properties.home", "D:/Projects/HomeAutomation/propertiesTest/");
    }

    @Before public void setup() {
        MockitoAnnotations.initMocks(this);
        //roomController = new RoomController();
        mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
    }

    @Test public void addRoomTest() throws Exception {
        Room room = new Room("Kitchen", 324);
        when(roomService.findById(1L)).thenReturn(room);
        when(roomService.save(room)).thenReturn(1L);

        Map<String, String> req = new HashMap<>();
        req.put("roomName", "Kitchen");
        req.put("squareFootage", "324");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/room").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(req)))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        Room retrieved = gson.fromJson(result.getResponse().getContentAsString(), Room.class);
        verify(roomService).save(Matchers.any(Room.class));
        assertEquals(room, retrieved);
    }

    @Test public void getAllRoomsTest() throws Exception {
        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(1L, "Kitchen", 156));
        rooms.add(new Room(2L, "Hall", 248));
        when(roomService.findAll()).thenReturn(rooms);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/room").contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        List retrieved = gson.fromJson(result.getResponse().getContentAsString(), List.class);
        verify(roomService).findAll();
        assertEquals(retrieved, rooms);
    }

    @Test public void updateRoomTest() throws Exception {
        Map<String, String> req = new HashMap<>();
        req.put("roomName", "Garden");
        req.put("squareFootage", "299");
        Room updatedRoom = new Room(req);
        updatedRoom.setId(1L);
        when(roomService.findById(1L)).thenReturn(updatedRoom);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/room/1").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(req)))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        Room retrieved = gson.fromJson(result.getResponse().getContentAsString(), Room.class);
        verify(roomService).save(updatedRoom);
        assertEquals(updatedRoom, retrieved);
    }

    @Test public void getRoomByIdTest() throws Exception {
        Room room = new Room("Hall", 78);
        when(roomService.findById(1L)).thenReturn(room);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/room/1").contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        Room retrieved = gson.fromJson(result.getResponse().getContentAsString(), Room.class);

        verify(roomService).findById(1L);
        assertEquals(room, retrieved);
    }

    @Test public void deleteRoomByIdTest() throws Exception {
        Room room = new Room("Hall", 78);
        when(roomService.findById(1L)).thenReturn(room);

        MvcResult result =mockMvc.perform(MockMvcRequestBuilders.delete("/room/1").contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();

        verify(roomService).findById(1L);
        verify(roomService).delete(Matchers.any(Room.class));
    }

    // _________________ Some tests for exceptions _________________

    @Test
    public void addRoomWithNoDataThrowsBadRequestExceptionTest() throws Exception {
        Map<String, String> req = new HashMap<>();
        req.put("badData", "Kitchen");
        req.put("squareFootage", "324");

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/room").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(req)));
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ApiErrorBadRequest: Can't make that request. Expected data format: {\"roomName\" : roomName, \"squareFootage\" : squareFootage}"));
        }
    }

    @Test
    public void findRoomWithBadIdThrowsNotFoundExceptionTest() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/room/9999").contentType(MediaType.APPLICATION_JSON));
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ApiErrorNotFound: Can't find room with 9999 id."));
        }
    }

    @Test
    public void updateRoomWithNoDataThrowsBadRequestExceptionTest() throws Exception {
        Map<String, String> req = new HashMap<>();
        req.put("roomName", "Kitchen");
        req.put("squareFootage", "324");
        Map<String, String> badReq = new HashMap<>();
        badReq.put("badData", "Kitchen");
        badReq.put("BadData!", "324");

        try {
            mockMvc.perform(MockMvcRequestBuilders.put("/room/1").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(badReq)));
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ApiErrorBadRequest: Can't make that request. Expected data format: {\"roomName\" : roomName, \"squareFootage\" : squareFootage}"));
        }

        try {
            mockMvc.perform(MockMvcRequestBuilders.put("/room/9999").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(req)));
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ApiErrorNotFound: Can't find room with 9999 id."));
        }
    }

    @Test
    public void deleteRoomWithBadIdThrowsNotFoundExceptionTest() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/room/9999").contentType(MediaType.APPLICATION_JSON));
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ApiErrorNotFound: Can't find room with 9999 id."));
        }
    }
}
