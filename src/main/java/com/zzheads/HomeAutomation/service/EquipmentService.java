package com.zzheads.HomeAutomation.service;//

import com.zzheads.HomeAutomation.model.Equipment;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.service created by zzheads on 20.08.2016.
//
public interface EquipmentService {
    List<Equipment> findAll();
    Equipment findById(Long id);
    Equipment findByName(String name);
    Long save(Equipment equipment);
    void delete(Equipment equipment);
}
