package com.zzheads.HomeAutomation.service;//

import com.zzheads.HomeAutomation.dao.ControlDao;
import com.zzheads.HomeAutomation.exceptions.DaoException;
import com.zzheads.HomeAutomation.model.Control;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.service created by zzheads on 20.08.2016.
//
@Service
public class ControlServiceImpl implements ControlService {

    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Autowired
    private ControlDao controlDao;

    @Override public List<Control> findAll() throws DaoException {
        return controlDao.findAll();
    }

    @Override public List<Control> findByEquipment(Long equipmentId) throws DaoException {
        return controlDao.findByEquipment(equipmentId);
    }

    @Override public Control findById(Long id) throws DaoException {
        return controlDao.findById(id);
    }

    @Override public Long save(Control control) throws DaoException {
        return controlDao.save(control);
    }

    @Override public void delete(Control control) throws DaoException {
        controlDao.delete(control);
    }
}
