package com.zzheads.HomeAutomation.service;//

import com.zzheads.HomeAutomation.dao.RoomDao;
import com.zzheads.HomeAutomation.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.service created by zzheads on 20.08.2016.
//
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomDao roomDao;

    @Override public List<Room> findAll() {
        return roomDao.findAll();
    }

    @Override public Room findById(Long id) {
        return roomDao.findById(id);
    }

    @Override public Room findByName(String name) {
        return roomDao.findByName(name);
    }

    @Override public Long save(Room room) {
        return roomDao.save(room);
    }

    @Override public void delete(Room room) {
        roomDao.delete(room);
    }
}
