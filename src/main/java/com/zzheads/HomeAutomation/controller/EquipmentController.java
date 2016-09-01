package com.zzheads.HomeAutomation.controller;//

import com.zzheads.HomeAutomation.exceptions.ApiErrorBadRequest;
import com.zzheads.HomeAutomation.exceptions.ApiErrorNotFound;
import com.zzheads.HomeAutomation.exceptions.DaoException;
import com.zzheads.HomeAutomation.model.Equipment;
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
// com.zzheads.HomeAutomation.controller created by zzheads on 21.08.2016.
//
@SuppressWarnings("SpringAutowiredFieldsWarningInspection")
@Controller
public class EquipmentController {
    @Autowired RoomService roomService;
    @Autowired EquipmentService equipmentService;
    @Autowired ControlService controlService;


    public static final String EXPECTED_REQUEST_FORMAT = "Can't make that request. Expected data format: {\"equipmentName\" : equipmentName}";
    private boolean requestOk (Map request) {
        return request.containsKey("equipmentName");
    }


    @RequestMapping(value = "/room/{roomId}/equipment", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String addEquipment(@RequestBody Map<String, String> req, @PathVariable Long roomId) throws DaoException {
        if (!requestOk(req)) throw new ApiErrorBadRequest(400, String.format("%s (%s)", EXPECTED_REQUEST_FORMAT, Thread.currentThread().getStackTrace()[1].toString()));
        Equipment equipment = new Equipment(req);
        Room room = roomService.findById(roomId);
        if (room == null) throw new ApiErrorNotFound(404, String.format("Can't find room with %d id. (%s)", roomId, Thread.currentThread().getStackTrace()[1].toString()));
        equipment.setRoom(room);
        room.getEquipments().add(equipment);
        equipmentService.save(equipment);
        roomService.save(room);
        return equipment.toJson();
    }

    @RequestMapping(value = "/room/{roomId}/equipment", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody String getAllEquipment(@PathVariable Long roomId) throws DaoException {
        List<Equipment> equipments = equipmentService.findByRoom(roomId);
        return Equipment.toJson(equipments);
    }

    @RequestMapping(value = "/room/{roomId}/equipment/{equipmentId}", method = RequestMethod.PUT, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody String updateEquipment(@RequestBody Map<String, String> req, @PathVariable Long roomId, @PathVariable Long equipmentId) throws DaoException {
        if (!requestOk(req)) throw new ApiErrorBadRequest(400, String.format("%s (%s)", EXPECTED_REQUEST_FORMAT, Thread.currentThread().getStackTrace()[1].toString()));
        Room room = roomService.findById(roomId);
        if (room == null) throw new ApiErrorNotFound(404, String.format("Can't find room with %d id. (updateEquipment method)", roomId));
        Equipment equipmentOld = equipmentService.findById(equipmentId);
        if (equipmentOld == null) throw new ApiErrorNotFound(404, String.format("Can't find equipment with %d id. (%s)", equipmentId, Thread.currentThread().getStackTrace()[1].toString()));
        Equipment equipment = new Equipment(req);
        equipment.setId(equipmentId);
        equipment.setRoom(room);
        room.removeEquipment(equipmentOld);
        room.addEquipment(equipment);
        equipmentService.save(equipment);
        roomService.save(room);
        return equipment.toJson();
    }

    @RequestMapping(value = "/room/{roomId}/equipment/{equipmentId}", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody String getEquipmentById(@PathVariable Long roomId, @PathVariable Long equipmentId) throws DaoException {
        Room room = roomService.findById(roomId);
        if (room == null) throw new ApiErrorNotFound(404, String.format("Can't find room with %d id. (%s)", roomId, Thread.currentThread().getStackTrace()[1].toString()));
        Equipment equipment = equipmentService.findById(equipmentId);
        if (equipment == null) throw new ApiErrorNotFound(404, String.format("Can't find equipment with %d id. (%s)", equipmentId, Thread.currentThread().getStackTrace()[1].toString()));
        return equipment.toJson();
    }

    @RequestMapping(value = "/room/{roomId}/equipment/{equipmentId}", method = RequestMethod.DELETE, produces = {"application/json"})
    @ResponseStatus (HttpStatus.NO_CONTENT)
    public void deleteEquipmentById(@PathVariable Long roomId, @PathVariable Long equipmentId) throws DaoException {
        Room room = roomService.findById(roomId);
        if (room == null) throw new ApiErrorNotFound(404, String.format("Can't find room with %d id. (%s)", roomId, Thread.currentThread().getStackTrace()[1].toString()));
        Equipment equipment = equipmentService.findById(equipmentId);
        if (equipment == null) throw new ApiErrorNotFound(404, String.format("Can't find equipment with %d id. (%s)", equipmentId, Thread.currentThread().getStackTrace()[1].toString()));
        room.removeEquipment(equipment);
        equipmentService.delete(equipment);
        roomService.save(room);
    }
}
