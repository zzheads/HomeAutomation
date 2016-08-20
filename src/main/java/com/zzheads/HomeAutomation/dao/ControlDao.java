package com.zzheads.HomeAutomation.dao;//

import com.zzheads.HomeAutomation.model.Control;
import com.zzheads.HomeAutomation.model.Room;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.dao created by zzheads on 20.08.2016.
//
public interface ControlDao {
    List<Control> findAll();
    Control findById(Long id);
    Control findByName(String name);
    Long save(Control control);
    void delete(Control control);
}
