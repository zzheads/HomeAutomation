package com.zzheads.HomeAutomation.dao;//

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.dao created by zzheads on 20.08.2016.
//
public interface CrudDao {
    Iterable findAll(Class mClass);
    Object findById(Class mClass, Long id);
    Object findByName(Class mClass, String name);
    void save(Object o);
    void delete(Object o);
}
