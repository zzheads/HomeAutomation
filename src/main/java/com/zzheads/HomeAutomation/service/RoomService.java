package com.zzheads.HomeAutomation.service;//

import com.zzheads.HomeAutomation.exceptions.DaoException;
import com.zzheads.HomeAutomation.model.Room;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.service created by zzheads on 20.08.2016.
//
public interface RoomService {
    List<Room> findAll() throws DaoException;
    Room findById(Long id) throws DaoException;
    Long save(Room room) throws DaoException;
    void delete(Room room) throws DaoException;
}
