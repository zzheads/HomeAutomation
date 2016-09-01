package com.zzheads.HomeAutomation.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zzheads.HomeAutomation.Application;
import com.zzheads.HomeAutomation.exceptions.ApiError;
import com.zzheads.HomeAutomation.exceptions.ApiErrorBadRequest;
import com.zzheads.HomeAutomation.exceptions.ApiErrorNotFound;
import com.zzheads.HomeAutomation.model.Room;
import com.zzheads.HomeAutomation.service.RoomService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.Assert.assertEquals;


@SuppressWarnings("SpringJavaAutowiringInspection")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ExceptionControllerTest {
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    RoomService roomService;

    private MockMvc mockMvc;

    static {
        System.setProperty("properties.home", "/Users/alexeypapin/IdeaProjects/HomeAutomation/propertiesTest/");
    }

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @SuppressWarnings({"ThrowableInstanceNeverThrown", "ThrowableResultOfMethodCallIgnored"})
    @Test
    public void checkStatusAndMessagesByExceptionHandlersTest() throws Exception {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Room.class, new Room.RoomSerializer())
            .registerTypeAdapter(Room.class, new Room.RoomDeserializer())
            .registerTypeAdapter(List.class, new Room.ListRoomSerializer())
            .registerTypeAdapter(List.class, new Room.ListRoomDeserializer())
            .setPrettyPrinting()
            .create();

        final ApiError errorNotFound = new ApiError(404, "Can't find room with 999 id. (com.zzheads.HomeAutomation.controller.RoomController.getRoomById(RoomController.java:69))", "/room/999");
        final ApiError errorBadRequest = new ApiError(400, "Can't make that request. Expected data format: {\"roomName\" : roomName, \"squareFootage\" : squareFootage} (com.zzheads.HomeAutomation.controller.RoomController.addRoom(RoomController.java:39))", "/room");
        ApiErrorNotFound errorNF;
        ApiErrorBadRequest errorBR;

        Room room = new Room("Some tests", 200);
        List<Room> rooms = new ArrayList<>();
        rooms.add(room);
        Long id = room.getId();
        roomService.save(room);
        assertNotEquals(id, room.getId());

        MvcResult result = this.mockMvc.perform(get("/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        assertEquals(gson.toJson(rooms, List.class), result.getResponse().getContentAsString());

        result = this.mockMvc.perform(get("/room/999").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andReturn();

        errorNF = ApiErrorNotFound.fromJson(result.getResponse().getContentAsString());
        assertEquals(errorNotFound, errorNF);

        Map<String, String> badReq = new HashMap<>();
        badReq.put("There is no roomName", "Testing Room");
        badReq.put("square Footage Bad", "345");

        result = this.mockMvc.perform(post("/room").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(badReq)))
            .andExpect(status().isBadRequest())
            .andReturn();

        errorBR = ApiErrorBadRequest.fromJson(result.getResponse().getContentAsString());
        assertEquals(errorBadRequest, errorBR);

    }
}
