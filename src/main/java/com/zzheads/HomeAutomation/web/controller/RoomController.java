package com.zzheads.HomeAutomation.web.controller;//

import com.zzheads.HomeAutomation.model.Room;
import com.zzheads.HomeAutomation.service.ControlService;
import com.zzheads.HomeAutomation.service.EquipmentService;
import com.zzheads.HomeAutomation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// HomeAutomation
// com.zzheads.HomeAutomation.web.controller created by zzheads on 20.08.2016.
//
@Controller
public class RoomController {
    @Autowired
    RoomService roomService;
    @Autowired
    EquipmentService equipmentService;
    @Autowired
    ControlService controlService;

    public static final String BASE_URL ="http://localhost:8080/";

    @RequestMapping(value = "/room", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody Map addRoom(@RequestBody Map<String, String> req) {
        Room room = new Room(req);
        roomService.save(room);
        return room.Json();
    }

    @RequestMapping(value = "/room", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody List getAllRooms() {
        List<Room> rooms = roomService.findAll();

        return Room.Json(rooms);
    }

    @RequestMapping(value = "/room/{id}", method = RequestMethod.PUT, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody Map updateRoom(@RequestBody Map<String, String> req, @PathVariable Long id) {
        Room room = new Room(req);
        room.setId(id);
        roomService.save(room);
        return room.Json();
    }

    @RequestMapping(value = "/room/{id}", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody Map getRoomById(@PathVariable Long id) {
        Room room = roomService.findById(id);

        return room.Json();
    }

    @RequestMapping(value = "/room/{id}", method = RequestMethod.DELETE, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody Map deleteRoomById(@PathVariable Long id) {
        Room room = roomService.findById(id);
        roomService.delete(room);

        return null;
    }
}
