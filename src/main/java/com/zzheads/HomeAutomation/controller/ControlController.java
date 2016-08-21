package com.zzheads.HomeAutomation.controller;//

import com.zzheads.HomeAutomation.model.Control;
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
public class ControlController {
    @Autowired RoomService roomService;
    @Autowired EquipmentService equipmentService;
    @Autowired ControlService controlService;

    @RequestMapping(value = "/room/{roomId}/equipment/{equipmentId}/control", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Map addControl(@RequestBody Map<String, String> req, @PathVariable Long roomId, @PathVariable Long equipmentId) {
        Equipment equipment = equipmentService.findById(equipmentId);
        Control control = new Control(req);
        control.setEquipment(equipment);
        equipment.addControl(control);
        controlService.save(control);
        return control.Json();
    }

    @RequestMapping(value = "/room/{roomId}/equipment/{equipmentId}/control", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody List getAllControls(@PathVariable Long roomId, @PathVariable Long equipmentId) {
        return Control.Json(controlService.findByEquipment(equipmentId));
    }

    @RequestMapping(value = "/room/{roomId}/equipment/{equipmentId}/control/{controlId}", method = RequestMethod.PUT, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody Map updateControl(@RequestBody Map<String, String> req, @PathVariable Long roomId, @PathVariable Long equipmentId, @PathVariable Long controlId) {
        Equipment equipment = equipmentService.findById(equipmentId);
        Control controlOld = controlService.findById(controlId);
        Control control = new Control(req);
        control.setId(controlId);
        control.setEquipment(equipment);
        equipment.removeControl(controlOld);
        equipment.addControl(control);
        controlService.save(control);
        equipmentService.save(equipment);
        return controlService.findById(controlId).Json();
    }

    @RequestMapping(value = "/room/{roomId}/equipment/{equipmentId}/control/{controlId}", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody Map getControlById(@PathVariable Long roomId, @PathVariable Long equipmentId, @PathVariable Long controlId) {
        return controlService.findById(controlId).Json();
    }

    @RequestMapping(value = "/room/{roomId}/equipment/{equipmentId}/control/{controlId}", method = RequestMethod.DELETE, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody Map deleteControlById(@PathVariable Long roomId, @PathVariable Long equipmentId, @PathVariable Long controlId) {
        Equipment equipment = equipmentService.findById(equipmentId);
        Control control = controlService.findById(controlId);
        equipment.removeControl(control);
        controlService.delete(control);
        equipmentService.save(equipment);

        return null;
    }

    @RequestMapping(value = "/room/{roomId}/equipment/{equipmentId}/control/{controlId}/value", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Map setValue(@RequestBody String req, @PathVariable Long roomId, @PathVariable Long equipmentId, @PathVariable Long controlId) {
        Control control = controlService.findById(controlId);
        control.setValue(req);
        controlService.save(control);
        return controlService.findById(controlId).jsonValue();
    }

    @RequestMapping(value = "/room/{roomId}/equipment/{equipmentId}/control/{controlId}/value", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Map getValue(@PathVariable Long roomId, @PathVariable Long equipmentId, @PathVariable Long controlId) {
        Control control = controlService.findById(controlId);
        return control.jsonValue();
    }

}
