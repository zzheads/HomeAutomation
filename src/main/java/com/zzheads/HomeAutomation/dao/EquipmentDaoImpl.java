package com.zzheads.HomeAutomation.dao;//

import com.zzheads.HomeAutomation.exceptions.DaoException;
import com.zzheads.HomeAutomation.model.Control;
import com.zzheads.HomeAutomation.model.Equipment;
import com.zzheads.HomeAutomation.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// HomeAutomation
// com.zzheads.HomeAutomation.dao created by zzheads on 20.08.2016.
//
@Repository
class EquipmentDaoImpl extends CrudDaoImpl implements EquipmentDao {

    @SuppressWarnings("unchecked")
    @Override public List<Equipment> findAll() throws DaoException {
        return super.findAll(Equipment.class);
    }

    @Override public List<Equipment> findByRoom(Long roomId) throws DaoException {
        return findAll().stream().filter(e -> Objects.equals(e.getRoom().getId(), roomId)).collect(Collectors.toList());
    }

    @Override public Equipment findById(Long id) throws DaoException {
        return (Equipment) super.findById(Equipment.class, id);
    }

    @Override public Long save(Equipment equipment) throws DaoException {
        super.save(equipment);
        return equipment.getId();
    }

    @Override public void delete(Equipment equipment) throws DaoException {
        super.delete(equipment);
    }
}
