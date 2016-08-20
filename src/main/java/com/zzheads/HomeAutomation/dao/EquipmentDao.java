package com.zzheads.HomeAutomation.dao;//

import com.zzheads.HomeAutomation.model.Control;
import com.zzheads.HomeAutomation.model.Equipment;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.dao created by zzheads on 20.08.2016.
//
public interface EquipmentDao {
    List<Equipment> findAll();
    Equipment findById(Long id);
    Equipment findByName(String name);
    Long save(Equipment equipment);
    void delete(Equipment equipment);
}
