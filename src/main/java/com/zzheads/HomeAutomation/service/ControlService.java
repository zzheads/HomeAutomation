package com.zzheads.HomeAutomation.service;//

import com.zzheads.HomeAutomation.exceptions.DaoException;
import com.zzheads.HomeAutomation.model.Control;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.service created by zzheads on 20.08.2016.
//
public interface ControlService {
    List<Control> findAll() throws DaoException;
    List<Control> findByEquipment(Long equipmentId) throws DaoException;
    Control findById(Long id) throws DaoException;
    Long save(Control control) throws DaoException;
    void delete(Control control) throws DaoException;
}
