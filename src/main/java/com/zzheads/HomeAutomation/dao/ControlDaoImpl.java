package com.zzheads.HomeAutomation.dao;//

import com.zzheads.HomeAutomation.model.Control;
import com.zzheads.HomeAutomation.model.Room;
import org.springframework.stereotype.Repository;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.dao created by zzheads on 20.08.2016.
//
@Repository
public class ControlDaoImpl extends CrudDaoImpl implements ControlDao {
    @SuppressWarnings("unchecked")
    @Override public List<Control> findAll() {
        return super.findAll(Control.class);
    }

    @Override public Control findById(Long id) {
        return (Control) super.findById(Control.class, id);
    }

    @Override public Control findByName(String name) {
        return (Control) super.findByName(Control.class, name);
    }

    @Override public Long save(Control control) {
        super.save(control);
        return control.getId();
    }

    @Override public void delete(Control control) {
        super.delete(control);
    }

}
