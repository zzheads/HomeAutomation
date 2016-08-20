package com.zzheads.HomeAutomation.dao;//

import com.zzheads.HomeAutomation.model.Room;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.dao created by zzheads on 20.08.2016.
//
public interface RoomDao {
    List<Room> findAll();
    Room findById(Long id);
    Room findByName(String name);
    Long save(Room room);
    void delete(Room room);
}
