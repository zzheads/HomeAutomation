package com.zzheads.HomeAutomation.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzheads.HomeAutomation.Application;
import com.zzheads.HomeAutomation.exceptions.DaoException;
import com.zzheads.HomeAutomation.model.Control;
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

import java.math.BigDecimal;
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
public class ControlControllerTest {

    final String BASE_URL = "http://localhost:8080/";

    @Mock private MockHttpSession session;

    @Mock private RoomService roomService;
    @Mock private EquipmentService equipmentService;
    @Mock private ControlService controlService;
    @InjectMocks private ControlController controlController;

    private MockMvc mockMvc;
    private Gson gson = new GsonBuilder()
        .registerTypeAdapter(Control.class, new Control.ControlSerializer())
        .registerTypeAdapter(Control.class, new Control.ControlDeserializer())
        .registerTypeAdapter(List.class, new Control.ListControlSerializer())
        .registerTypeAdapter(List.class, new Control.ListControlDeserializer())
        .create();
    private Gson gsonForValue = new GsonBuilder()
        .registerTypeAdapter(Control.class, new Control.ControlValueSerializer())
        .registerTypeAdapter(Control.class, new Control.ControlValueDeserializer())
        .create();

    private Room testRoom = new Room(1L, "Name of test Room", 100);
    private Equipment testEquipment = new Equipment(1L, "Name of test Equipment");
    private Control testControl = new Control(1L, "Name of test Control");

    static {
        System.setProperty("properties.home", "E:/Projects/HomeAutomation/propertiesTest/");
    }

    @Before public void setup() throws DaoException {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controlController).build();
        testRoom.addEquipment(testEquipment);
        testEquipment.setRoom(testRoom);
        testEquipment.addControl(testControl);
        testControl.setEquipment(testEquipment);
        testControl.setValue(BigDecimal.valueOf(45.59));

        when(roomService.findById(1L)).thenReturn(testRoom);
        when(equipmentService.findById(1L)).thenReturn(testEquipment);
        when(controlService.findById(1L)).thenReturn(testControl);
    }

    @Test public void addControlTest() throws Exception {
        Map<String, String> req = new HashMap<>();
        req.put("controlName", "Thermostat");
        Control control = new Control(req);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/room/1/equipment/1/control").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(req)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn();

        Control retrieved = gson.fromJson(result.getResponse().getContentAsString(), Control.class);
        verify(controlService).save(Matchers.any(Control.class));
        assertEquals(control, retrieved);
    }

    @SuppressWarnings("unchecked")
    @Test public void getAllControlTest() throws Exception {
        List<Control> controls = new ArrayList<>();
        controls.add(testControl);
        when(controlService.findByEquipment(testEquipment.getId())).thenReturn(controls);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/room/1/equipment/1/control").contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        List<Control> retrieved = gson.fromJson(result.getResponse().getContentAsString(), List.class);
        retrieved.get(0).setValue(testControl.getValue());
        retrieved.get(0).setEquipment(testControl.getEquipment());

        verify(controlService).findByEquipment(1L);
        assertEquals(controls, retrieved);
    }

    @Test public void updateControlTest() throws Exception {
        Map<String, String> req = new HashMap<>();
        req.put("controlName", "Thermostat");
        Control updatedControl = new Control(req);
        updatedControl.setEquipment(testEquipment);
        when(controlService.findById(1L)).thenReturn(updatedControl);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/room/1/equipment/1/control/1").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(req)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn();

        Control retrieved = gson.fromJson(result.getResponse().getContentAsString(), Control.class);
        retrieved.setValue(updatedControl.getValue());
        retrieved.setEquipment(updatedControl.getEquipment());

        assertEquals(updatedControl, retrieved);
        verify(controlService).save(Matchers.any(Control.class));
    }

    @Test public void getControlByIdTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/room/1/equipment/1/control/1").contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        Control retrieved = gson.fromJson(result.getResponse().getContentAsString(), Control.class);
        // Since Json dont send these values
        retrieved.setValue(testControl.getValue());
        retrieved.setEquipment(testControl.getEquipment());

        verify(controlService).findById(1L);
        assertEquals(testControl, retrieved);
    }

    @Test public void deleteControlByIdTest() throws Exception {

        MvcResult result =mockMvc.perform(MockMvcRequestBuilders.delete("/room/1/equipment/1/control/1").contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();

        verify(controlService).findById(1L);
        verify(controlService).delete(testControl);
    }

    // _________________ Some tests for exceptions _________________

    @Test
    public void addControlWithNoDataThrowsBadRequestExceptionTest() throws Exception {
        Map<String, String> req = new HashMap<>();
        req.put("badData", "Bad data");

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/room/1/equipment/1/control").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(req)));
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ApiErrorBadRequest: Can't make that request. Expected data format: {\"controlName\" : controlName}"));
        }
    }

    @Test
    public void findControlWithBadIdThrowsNotFoundExceptionTest() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/room/1/equipment/1/control/9999").contentType(MediaType.APPLICATION_JSON));
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ApiErrorNotFound: Can't find control with 9999 id."));
        }
    }

    @Test
    public void updateControlWithNoDataThrowsBadRequestExceptionTest() throws Exception {
        Map<String, String> req = new HashMap<>();
        req.put("controlName", "New name of control");
        Map<String, String> badReq = new HashMap<>();
        badReq.put("badData", "Data bad!");

        try {
            mockMvc.perform(MockMvcRequestBuilders.put("/room/1/equipment/1/control/1").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(badReq)));
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ApiErrorBadRequest: Can't make that request. Expected data format: {\"controlName\" : controlName}"));
        }

        try {
            mockMvc.perform(MockMvcRequestBuilders.put("/room/1/equipment/1/control/9999").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(req)));
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ApiErrorNotFound: Can't find control with 9999 id."));
        }
    }

    @Test
    public void deleteControlWithBadIdThrowsNotFoundExceptionTest() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/room/1/equipment/9999/control/1").contentType(MediaType.APPLICATION_JSON));
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ApiErrorNotFound: Can't find equipment with 9999 id."));
        }

        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/room/1/equipment/1/control/9999").contentType(MediaType.APPLICATION_JSON));
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ApiErrorNotFound: Can't find control with 9999 id."));
        }
    }
}
