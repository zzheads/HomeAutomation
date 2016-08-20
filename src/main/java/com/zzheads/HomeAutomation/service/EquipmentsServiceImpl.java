package com.zzheads.HomeAutomation.service;//

import com.zzheads.HomeAutomation.dao.EquipmentDao;
import com.zzheads.HomeAutomation.model.Equipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.service created by zzheads on 20.08.2016.
//
@Service
public class EquipmentsServiceImpl implements EquipmentService {
    @Autowired
    private EquipmentDao equipmentDao;

    @Override public List<Equipment> findAll() {
        return equipmentDao.findAll();
    }

    @Override public List<Equipment> findByRoom(Long roomId) {
        return equipmentDao.findByRoom(roomId);
    }

    @Override public Equipment findById(Long id) {
        return equipmentDao.findById(id);
    }

    @Override public Equipment findByName(String name) {
        return equipmentDao.findByName(name);
    }

    @Override public Long save(Equipment equipment) {
        return equipmentDao.save(equipment);
    }

    @Override public void delete(Equipment equipment) {
        equipmentDao.delete(equipment);
    }
}
