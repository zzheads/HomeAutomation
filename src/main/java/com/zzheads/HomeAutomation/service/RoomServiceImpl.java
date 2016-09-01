package com.zzheads.HomeAutomation.service;//

import com.zzheads.HomeAutomation.dao.RoomDao;
import com.zzheads.HomeAutomation.exceptions.DaoException;
import com.zzheads.HomeAutomation.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.service created by zzheads on 20.08.2016.
//
@Service
public class RoomServiceImpl implements RoomService {

    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Autowired
    private RoomDao roomDao;

    @Override public List<Room> findAll() throws DaoException {
        return roomDao.findAll();
    }

    @Override public Room findById(Long id) throws DaoException {
        return roomDao.findById(id);
    }

    @Override public Long save(Room room) throws DaoException {
        return roomDao.save(room);
    }

    @Override public void delete(Room room) throws DaoException {
        roomDao.delete(room);
    }
}
