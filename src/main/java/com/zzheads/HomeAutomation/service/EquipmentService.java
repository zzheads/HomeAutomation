package com.zzheads.HomeAutomation.service;//

import com.zzheads.HomeAutomation.exceptions.DaoException;
import com.zzheads.HomeAutomation.model.Equipment;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.service created by zzheads on 20.08.2016.
//
public interface EquipmentService {
    List<Equipment> findAll() throws DaoException;
    List<Equipment> findByRoom(Long roomId) throws DaoException;
    Equipment findById(Long id) throws DaoException;
    Long save(Equipment equipment) throws DaoException;
    void delete(Equipment equipment) throws DaoException;
}
