package com.zzheads.HomeAutomation.service;

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
public class EquipmentServiceTest {
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    ControlService controlService;
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    EquipmentService equipmentService;
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    RoomService roomService;


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
        equipmentService.save(equipment);
        List<Equipment> equipments = equipmentService.findAll();
        assertEquals(1, equipments.size());
    }

    @Test public void testFindByRoom() throws Exception {
        Equipment equipment = new Equipment();
        equipmentService.save(equipment);
        Room room = new Room("Test room", 325);
        roomService.save(room);
        room.addEquipment(equipment);
        equipment.setRoom(room);
        equipmentService.save(equipment);

        List<Equipment> equipments = equipmentService.findByRoom(room.getId());
        assertEquals(equipments.get(0), equipment);
    }

    @Test public void testFindById() throws Exception {
        Equipment equipment = newEquipment();
        equipmentService.save(equipment);
        Equipment newEquipment = equipmentService.findById(equipment.getId());
        assertEquals(equipment, newEquipment);
    }

    @Test public void testSave() throws Exception {
        Equipment equipment = newEquipment();
        Long id = equipment.getId();

        equipmentService.save(equipment);
        assertNotEquals(id, equipment.getId());
    }

    @Test public void testDelete() throws Exception {
        Equipment equipment = newEquipment();
        equipmentService.save(equipment);
        assertEquals(1, equipmentService.findAll().size());

        equipmentService.delete(equipment);
        assertEquals(0, equipmentService.findAll().size());
    }

    private Control newControl () {
        return new Control("Test control");
    }
    private Equipment newEquipment () {
        return new Equipment("Test equipment");
    }

    private void clearAll() throws DaoException {
        for (Control c : controlService.findAll())
            controlService.delete(c);
        for (Equipment e : equipmentService.findAll())
            equipmentService.delete(e);
        for (Room r : roomService.findAll())
            roomService.delete(r);
    }
}
