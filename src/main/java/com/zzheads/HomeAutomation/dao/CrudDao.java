package com.zzheads.HomeAutomation.dao;//

import com.zzheads.HomeAutomation.exceptions.DaoException;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.dao created by zzheads on 20.08.2016.
//
public interface CrudDao {
    Iterable findAll(Class mClass) throws DaoException;
    Object findById(Class mClass, Long id) throws DaoException;
    void save(Object o) throws DaoException;
    void delete(Object o) throws DaoException;
}
