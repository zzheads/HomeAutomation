package com.zzheads.HomeAutomation.service;

import com.zzheads.HomeAutomation.Application;
import com.zzheads.HomeAutomation.exceptions.DaoException;
import com.zzheads.HomeAutomation.model.Control;
import com.zzheads.HomeAutomation.model.Equipment;
import com.zzheads.HomeAutomation.model.Room;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

//
// HomeAutomation
// com.zzheads.HomeAutomation.service created by zzheads on 22.08.2016.
//
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ControlServiceTest {
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired ControlService controlService;
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired EquipmentService equipmentService;
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired RoomService roomService;

    static {
        System.setProperty("properties.home", "E:/Projects/HomeAutomation/propertiesTest/");
    }


    @Before public void setUp() throws Exception {

    }

    @After public void tearDown() throws Exception {
        clearAll();
    }

    @Test public void testFindAll() throws Exception {
        Control control = newControl();

        controlService.save(control);
        List<Control> controls = controlService.findAll();
        assertEquals(1, controls.size());
    }

    @Test public void testFindByEquipment() throws Exception {
        Equipment equipment = new Equipment();
        equipmentService.save(equipment);
        Control control = newControl();
        controlService.save(control);
        equipment.addControl(control);
        control.setEquipment(equipment);
        controlService.save(control);

        List<Control> controls = controlService.findByEquipment(equipment.getId());
        assertEquals(controls.get(0), control);
    }

    @Test public void testFindById() throws Exception {
        Control control = newControl();
        controlService.save(control);
        Control newControl = controlService.findById(control.getId());
        assertEquals(control, newControl);
    }

    @Test public void testSave() throws Exception {
        Control control = newControl();
        Long id = control.getId();

        controlService.save(control);
        assertNotEquals(id, control.getId());
    }

    @Test public void testDelete() throws Exception {
        Control control = newControl();
        controlService.save(control);
        assertEquals(1, controlService.findAll().size());

        controlService.delete(control);
        assertEquals(0, controlService.findAll().size());
    }

    private Control newControl () {
        return new Control("Test control");
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
