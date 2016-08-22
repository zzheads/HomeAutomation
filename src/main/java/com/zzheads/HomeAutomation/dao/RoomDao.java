package com.zzheads.HomeAutomation.dao;//

import com.zzheads.HomeAutomation.exceptions.DaoException;
import com.zzheads.HomeAutomation.model.Room;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.dao created by zzheads on 20.08.2016.
//
public interface RoomDao {
    List<Room> findAll() throws DaoException;
    Room findById(Long id) throws DaoException;
    Long save(Room room) throws DaoException;
    void delete(Room room) throws DaoException;
}
