package com.zzheads.HomeAutomation.controller;//

import com.zzheads.HomeAutomation.exceptions.ApiErrorBadRequest;
import com.zzheads.HomeAutomation.exceptions.ApiErrorNotFound;
import com.zzheads.HomeAutomation.exceptions.DaoException;
import com.zzheads.HomeAutomation.model.Room;
import com.zzheads.HomeAutomation.service.ControlService;
import com.zzheads.HomeAutomation.service.EquipmentService;
import com.zzheads.HomeAutomation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// HomeAutomation
// com.zzheads.HomeAutomation.web.controller created by zzheads on 20.08.2016.
//
@SuppressWarnings("SpringAutowiredFieldsWarningInspection")
@Controller
public class RoomController {
    @Autowired
    RoomService roomService;
    @Autowired
    EquipmentService equipmentService;
    @Autowired
    ControlService controlService;

    public static final String EXPECTED_REQUEST_FORMAT = "Can't make that request. Expected data format: {\"roomName\" : roomName, \"squareFootage\" : squareFootage}";
    private boolean requestOk (Map request) {
        return request.containsKey("roomName") && request.containsKey("squareFootage");
    }

    @RequestMapping(value = "/room", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody String addRoom(@RequestBody Map<String, String> req) throws DaoException {
        if (!requestOk(req)) throw new ApiErrorBadRequest(400, String.format("%s (%s)", EXPECTED_REQUEST_FORMAT, Thread.currentThread().getStackTrace()[1].toString()));
        Room room = new Room(req);
        roomService.save(room);
        return room.toJson();
    }

    @RequestMapping(value = "/room", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody String getAllRooms() throws DaoException {
        List<Room> rooms = roomService.findAll();
        return Room.toJson(rooms);
    }

    @RequestMapping(value = "/room/{id}", method = RequestMethod.PUT, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody String updateRoom(@RequestBody Map<String, String> req, @PathVariable Long id) throws DaoException {
        if (!requestOk(req)) throw new ApiErrorBadRequest(400, String.format("%s (%s)", EXPECTED_REQUEST_FORMAT, Thread.currentThread().getStackTrace()[1].toString()));
        Room updatingRoom = new Room(req);
        Room room = roomService.findById(id);
        if (room == null) throw new ApiErrorNotFound(404, String.format("Can't find room with %d id. (%s)", id, Thread.currentThread().getStackTrace()[1].toString()));
        room.setName(updatingRoom.getName());
        room.setSquareFootage(updatingRoom.getSquareFootage());
        roomService.save(room);
        return room.toJson();
    }

    @RequestMapping(value = "/room/{id}", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody String getRoomById(@PathVariable Long id) throws DaoException {
        Room room = roomService.findById(id);
        if (room == null) throw new ApiErrorNotFound(404, String.format("Can't find room with %d id. (%s)", id, Thread.currentThread().getStackTrace()[1].toString()));
        return room.toJson();
    }

    @RequestMapping(value = "/room/{id}", method = RequestMethod.DELETE, produces = {"application/json"})
    @ResponseStatus (HttpStatus.NO_CONTENT)
    public void deleteRoomById(@PathVariable Long id) throws DaoException {
        Room room = roomService.findById(id);
        if (room == null) throw new ApiErrorNotFound(404, String.format("Can't find room with %d id. (%s)", id, Thread.currentThread().getStackTrace()[1].toString()));
        roomService.delete(room);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody String getAll() throws DaoException {
        List<Room> rooms = roomService.findAll();
        return Room.toJson(rooms);
    }
}
