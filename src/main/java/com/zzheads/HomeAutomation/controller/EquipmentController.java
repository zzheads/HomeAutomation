package com.zzheads.HomeAutomation.controller;//

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
@Controller
public class EquipmentController {
    @Autowired RoomService roomService;
    @Autowired EquipmentService equipmentService;
    @Autowired ControlService controlService;

    @RequestMapping(value = "/room/{roomId}/equipment", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Map addEquipment(@RequestBody Map<String, String> req, @PathVariable Long roomId) {
        Equipment equipment = new Equipment(req);
        Room room = roomService.findById(roomId);
        equipment.setRoom(room);
        room.getEquipments().add(equipment);
        equipmentService.save(equipment);
        roomService.save(room);
        return equipment.Json();
    }

    @RequestMapping(value = "/room/{roomId}/equipment", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody List getAllEquipment(@PathVariable Long roomId) {
        return Equipment.Json(equipmentService.findByRoom(roomId));
    }

    @RequestMapping(value = "/room/{roomId}/equipment/{equipmentId}", method = RequestMethod.PUT, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody Map updateEquipment(@RequestBody Map<String, String> req, @PathVariable Long roomId, @PathVariable Long equipmentId) {
        Room room = roomService.findById(roomId);
        Equipment equipmentOld = equipmentService.findById(equipmentId);
        Equipment equipment = new Equipment(req);
        equipment.setId(equipmentId);
        equipment.setRoom(room);
        room.removeEquipment(equipmentOld);
        room.addEquipment(equipment);
        equipmentService.save(equipment);
        roomService.save(room);
        return equipmentService.findById(equipmentId).Json();
    }

    @RequestMapping(value = "/room/{roomId}/equipment/{equipmentId}", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody Map getEquipmentById(@PathVariable Long roomId, @PathVariable Long equipmentId) {
        Room room = roomService.findById(roomId);
        Equipment equipment = equipmentService.findById(equipmentId);

        return equipment.Json();
    }

    @RequestMapping(value = "/room/{roomId}/equipment/{equipmentId}", method = RequestMethod.DELETE, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody Map deleteEquipmentById(@PathVariable Long roomId, @PathVariable Long equipmentId) {
        Room room = roomService.findById(roomId);
        Equipment equipment = equipmentService.findById(equipmentId);
        room.removeEquipment(equipment);
        equipmentService.delete(equipment);
        roomService.save(room);

        return null;
    }

}
