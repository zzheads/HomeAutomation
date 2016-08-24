package com.zzheads.HomeAutomation.controller;//

import com.zzheads.HomeAutomation.exceptions.ApiErrorBadRequest;
import com.zzheads.HomeAutomation.exceptions.ApiErrorNotFound;
import com.zzheads.HomeAutomation.exceptions.DaoException;
import com.zzheads.HomeAutomation.model.Control;
import com.zzheads.HomeAutomation.model.Equipment;
import com.zzheads.HomeAutomation.service.ControlService;
import com.zzheads.HomeAutomation.service.EquipmentService;
import com.zzheads.HomeAutomation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// HomeAutomation
// com.zzheads.HomeAutomation.controller created by zzheads on 21.08.2016.
//
@Controller
public class ControlController {
    @Autowired RoomService roomService;
    @Autowired EquipmentService equipmentService;
    @Autowired ControlService controlService;

    public static final String EXPECTED_REQUEST_FORMAT = "Can't make that request. Expected data format: {\"controlName\" : controlName}";
    private static final String EXPECTED_REQUEST_FORMAT_VALUE = "Can't make that request. Expected data format: {\"value\"}";
    private boolean requestOk (Map request) {
        return request.containsKey("controlName");
    }
    private boolean requestOk (String request) {
        String str = Control.formattedString(request);
        return (str != null && str.length() > 0);
    }

    @RequestMapping(value = "/room/{roomId}/equipment/{equipmentId}/control", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String addControl(@RequestBody Map<String, String> req, @PathVariable Long roomId, @PathVariable Long equipmentId) throws DaoException {
        if (!requestOk(req)) throw new ApiErrorBadRequest(400, String.format("%s (%s)", EXPECTED_REQUEST_FORMAT, Thread.currentThread().getStackTrace()[1].toString()));
        Equipment equipment = equipmentService.findById(equipmentId);
        if (equipment == null) throw new ApiErrorNotFound(404, String.format("Can't find equipment with %d id. (%s)", equipmentId, Thread.currentThread().getStackTrace()[1].toString()));
        Control control = new Control(req);
        control.setEquipment(equipment);
        equipment.addControl(control);
        controlService.save(control);
        return control.toJson();
    }

    @RequestMapping(value = "/room/{roomId}/equipment/{equipmentId}/control", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody String getAllControls(@PathVariable Long roomId, @PathVariable Long equipmentId) throws DaoException {
        return Control.toJson(controlService.findByEquipment(equipmentId));
    }

    @RequestMapping(value = "/room/{roomId}/equipment/{equipmentId}/control/{controlId}", method = RequestMethod.PUT, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody String updateControl(@RequestBody Map<String, String> req, @PathVariable Long roomId, @PathVariable Long equipmentId, @PathVariable Long controlId) throws DaoException {
        if (!requestOk(req)) throw new ApiErrorBadRequest(400, String.format("%s (%s)", EXPECTED_REQUEST_FORMAT, Thread.currentThread().getStackTrace()[1].toString()));
        Equipment equipment = equipmentService.findById(equipmentId);
        if (equipment == null) throw new ApiErrorNotFound(404, String.format("Can't find equipment with %d id. (%s)", equipmentId, Thread.currentThread().getStackTrace()[1].toString()));
        Control controlOld = controlService.findById(controlId);
        if (controlOld == null) throw new ApiErrorNotFound(404, String.format("Can't find control with %d id. (%s)", controlId, Thread.currentThread().getStackTrace()[1].toString()));
        Control control = new Control(req);
        control.setId(controlId);
        control.setEquipment(equipment);
        equipment.removeControl(controlOld);
        equipment.addControl(control);
        controlService.save(control);
        equipmentService.save(equipment);
        return controlService.findById(controlId).toJson();
    }

    @RequestMapping(value = "/room/{roomId}/equipment/{equipmentId}/control/{controlId}", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody String getControlById(@PathVariable Long roomId, @PathVariable Long equipmentId, @PathVariable Long controlId) throws DaoException {
        Control control = controlService.findById(controlId);
        if (control == null) throw new ApiErrorNotFound(404, String.format("Can't find control with %d id. (%s)", controlId, Thread.currentThread().getStackTrace()[1].toString()));
        return control.toJson();
    }

    @RequestMapping(value = "/room/{roomId}/equipment/{equipmentId}/control/{controlId}", method = RequestMethod.DELETE, produces = {"application/json"})
    @ResponseStatus (HttpStatus.NO_CONTENT)
    public void deleteControlById(@PathVariable Long roomId, @PathVariable Long equipmentId, @PathVariable Long controlId) throws DaoException {
        Equipment equipment = equipmentService.findById(equipmentId);
        if (equipment == null) throw new ApiErrorNotFound(404, String.format("Can't find equipment with %d id. (%s)", equipmentId, Thread.currentThread().getStackTrace()[1].toString()));
        Control control = controlService.findById(controlId);
        if (control == null) throw new ApiErrorNotFound(404, String.format("Can't find control with %d id. (%s)", controlId, Thread.currentThread().getStackTrace()[1].toString()));
        equipment.removeControl(control);
        controlService.delete(control);
        equipmentService.save(equipment);
    }

    @RequestMapping(value = "/room/{roomId}/equipment/{equipmentId}/control/{controlId}/value", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String setValue(@RequestBody String req, @PathVariable Long roomId, @PathVariable Long equipmentId, @PathVariable Long controlId) throws DaoException {
        if (!requestOk(req)) throw new ApiErrorBadRequest(400, String.format("%s (%s)", EXPECTED_REQUEST_FORMAT_VALUE, Thread.currentThread().getStackTrace()[1].toString()));
        Control control = controlService.findById(controlId);
        if (control == null) throw new ApiErrorNotFound(404, String.format("Can't find control with %d id. (%s)", controlId, Thread.currentThread().getStackTrace()[1].toString()));
        control.setValue(req);
        controlService.save(control);
        return controlService.findById(controlId).toJsonValue();
    }

    @RequestMapping(value = "/room/{roomId}/equipment/{equipmentId}/control/{controlId}/value", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String getValue(@PathVariable Long roomId, @PathVariable Long equipmentId, @PathVariable Long controlId) throws DaoException {
        Control control = controlService.findById(controlId);
        if (control == null) throw new ApiErrorNotFound(404, String.format("Can't find control with %d id. (%s)", controlId, Thread.currentThread().getStackTrace()[1].toString()));
        if (control.getValue() == null) throw new ApiErrorNotFound(404, String.format("Can't find value with %d id. (%s)", controlId, Thread.currentThread().getStackTrace()[1].toString()));
        return control.toJsonValue();
    }
}
