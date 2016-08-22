package com.zzheads.HomeAutomation.dao;

import com.zzheads.HomeAutomation.Application;
import com.zzheads.HomeAutomation.exceptions.DaoException;
import com.zzheads.HomeAutomation.model.Control;
import com.zzheads.HomeAutomation.model.Equipment;
import com.zzheads.HomeAutomation.model.Room;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.Assert.*;

//
// HomeAutomation
// com.zzheads.HomeAutomation.dao created by zzheads on 22.08.2016.
//
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class EquipmentDaoTest {
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    ControlDao controlDao;
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    EquipmentDao equipmentDao;
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    RoomDao roomDao;


    static {
        System.setProperty("properties.home", "E:/Projects/HomeAutomation/propertiesTest/");
    }

    @Before public void setUp() throws Exception {

    }

    @After public void tearDown() throws Exception {
        clearAll();
    }

    @Test public void testFindAll() throws Exception {
        Equipment equipment = newEquipment();
        List<Equipment> equipments1 = equipmentDao.findAll();
        equipmentDao.save(equipment);
        List<Equipment> equipments = equipmentDao.findAll();
        assertEquals(1, equipments.size());
    }

    @Test public void testFindByRoom() throws Exception {
        Equipment equipment = new Equipment();
        equipmentDao.save(equipment);
        Room room = new Room("Test room", 325);
        roomDao.save(room);
        room.addEquipment(equipment);
        equipment.setRoom(room);
        equipmentDao.save(equipment);

        List<Equipment> equipments = equipmentDao.findByRoom(room.getId());
        assertEquals(equipments.get(0), equipment);
    }

    @Test public void testFindById() throws Exception {
        Equipment equipment = newEquipment();
        equipmentDao.save(equipment);
        Equipment newEquipment = equipmentDao.findById(equipment.getId());
        assertEquals(equipment, newEquipment);
    }

    @Test public void testSave() throws Exception {
        Equipment equipment = newEquipment();
        Long id = equipment.getId();

        equipmentDao.save(equipment);
        assertNotEquals(id, equipment.getId());
    }

    @Test public void testDelete() throws Exception {
        Equipment equipment = newEquipment();
        equipmentDao.save(equipment);
        assertEquals(1, equipmentDao.findAll().size());

        equipmentDao.delete(equipment);
        assertEquals(0, equipmentDao.findAll().size());
    }

    private Control newControl () {
        return new Control("Test control");
    }
    private Equipment newEquipment () {
        return new Equipment("Test equipment");
    }

    private void clearAll() throws DaoException {
        for (Control c : controlDao.findAll())
            controlDao.delete(c);
        for (Equipment e : equipmentDao.findAll())
            equipmentDao.delete(e);
        for (Room r : roomDao.findAll())
            roomDao.delete(r);
    }

}
