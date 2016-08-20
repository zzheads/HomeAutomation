package com.zzheads.HomeAutomation.service;//

import com.zzheads.HomeAutomation.dao.ControlDao;
import com.zzheads.HomeAutomation.model.Control;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.service created by zzheads on 20.08.2016.
//
@Service
public class ControlServiceImpl implements ControlService {
    @Autowired
    private ControlDao controlDao;

    @Override public List<Control> findAll() {
        return controlDao.findAll();
    }

    @Override public Control findById(Long id) {
        return controlDao.findById(id);
    }

    @Override public Control findByName(String name) {
        return controlDao.findByName(name);
    }

    @Override public Long save(Control control) {
        return controlDao.save(control);
    }

    @Override public void delete(Control control) {
        controlDao.delete(control);
    }
}
