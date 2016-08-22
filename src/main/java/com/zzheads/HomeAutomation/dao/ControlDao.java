package com.zzheads.HomeAutomation.dao;//

import com.zzheads.HomeAutomation.exceptions.DaoException;
import com.zzheads.HomeAutomation.model.Control;
import com.zzheads.HomeAutomation.model.Equipment;
import com.zzheads.HomeAutomation.model.Room;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.dao created by zzheads on 20.08.2016.
//
public interface ControlDao {
    List<Control> findAll() throws DaoException;
    List<Control> findByEquipment(Long equipmentId) throws DaoException;
    Control findById(Long id) throws DaoException;
    Long save(Control control) throws DaoException;
    void delete(Control control) throws DaoException;
}
