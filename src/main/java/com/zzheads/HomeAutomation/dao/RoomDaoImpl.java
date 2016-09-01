package com.zzheads.HomeAutomation.dao;//

import com.zzheads.HomeAutomation.exceptions.DaoException;
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
class RoomDaoImpl extends CrudDaoImpl implements RoomDao {
    @SuppressWarnings("unchecked")
    @Override public List<Room> findAll() throws DaoException {
        return super.findAll(Room.class);
    }

    @Override public Room findById(Long id) throws DaoException {
        return (Room) super.findById(Room.class, id);
    }

    @Override public Long save(Room room) throws DaoException {
        super.save(room);
        return room.getId();
    }

    @Override public void delete(Room room) throws DaoException {
        super.delete(room);
    }
}
