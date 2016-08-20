package com.zzheads.HomeAutomation.service;//

import com.zzheads.HomeAutomation.model.Room;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.service created by zzheads on 20.08.2016.
//
public interface RoomService {
    List<Room> findAll();
    Room findById(Long id);
    Room findByName(String name);
    Long save(Room room);
    void delete(Room room);
}
