package com.zzheads.HomeAutomation.dao;//

import com.zzheads.HomeAutomation.model.Control;
import com.zzheads.HomeAutomation.model.Equipment;
import com.zzheads.HomeAutomation.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.dao created by zzheads on 20.08.2016.
//
@Repository
public class EquipmentDaoImpl extends CrudDaoImpl implements EquipmentDao {

    @SuppressWarnings("unchecked")
    @Override public List<Equipment> findAll() {
        return super.findAll(Equipment.class);
    }

    @Override public Equipment findById(Long id) {
        return (Equipment) super.findById(Equipment.class, id);
    }

    @Override public Equipment findByName(String name) {
        return (Equipment) super.findByName(Equipment.class, name);
    }

    @Override public Long save(Equipment equipment) {
        super.save(equipment);
        return equipment.getId();
    }

    @Override public void delete(Equipment equipment) {
        super.delete(equipment);
    }
}
