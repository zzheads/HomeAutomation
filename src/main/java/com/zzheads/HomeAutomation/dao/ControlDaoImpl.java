package com.zzheads.HomeAutomation.dao;//

import com.zzheads.HomeAutomation.exceptions.DaoException;
import com.zzheads.HomeAutomation.model.Control;
import com.zzheads.HomeAutomation.model.Room;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// HomeAutomation
// com.zzheads.HomeAutomation.dao created by zzheads on 20.08.2016.
//
@Repository
class ControlDaoImpl extends CrudDaoImpl implements ControlDao {
    @SuppressWarnings("unchecked")
    @Override public List<Control> findAll() throws DaoException {
        return super.findAll(Control.class);
    }

    @Override public List<Control> findByEquipment(Long equipmentId) throws DaoException {
        return findAll().stream().filter(c -> Objects.equals(c.getEquipment().getId(), equipmentId)).collect(Collectors.toList());
    }

    @Override public Control findById(Long id) throws DaoException {
        return (Control) super.findById(Control.class, id);
    }

    @Override public Long save(Control control) throws DaoException {
        super.save(control);
        return control.getId();
    }

    @Override public void delete(Control control) throws DaoException {
        super.delete(control);
    }

}
