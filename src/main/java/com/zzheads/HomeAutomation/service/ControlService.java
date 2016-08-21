package com.zzheads.HomeAutomation.service;//

import com.zzheads.HomeAutomation.model.Control;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.service created by zzheads on 20.08.2016.
//
public interface ControlService {
    List<Control> findAll();
    List<Control> findByEquipment(Long equipmentId);
    Control findById(Long id);
    Control findByName(String name);
    Long save(Control control);
    void delete(Control control);
}
