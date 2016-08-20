package com.zzheads.HomeAutomation.dao;//

import com.zzheads.HomeAutomation.model.Control;
import com.zzheads.HomeAutomation.model.Equipment;
import com.zzheads.HomeAutomation.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.dao created by zzheads on 20.08.2016.
//
@Repository
public class RoomDaoImpl extends CrudDaoImpl implements RoomDao {
    @SuppressWarnings("unchecked")
    @Override public List<Room> findAll() {
        return super.findAll(Room.class);
    }

    @Override public Room findById(Long id) {
        return (Room) super.findById(Room.class, id);
    }

    @Override public Room findByName(String name) {
        return (Room) super.findByName(Room.class, name);
    }

    @Override public Long save(Room room) {
        super.save(room);
        return room.getId();
    }

    @Override public void delete(Room room) {
        super.delete(room);
    }
}
