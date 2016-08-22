package com.zzheads.HomeAutomation.controller;

import com.google.gson.Gson;
import com.zzheads.HomeAutomation.Application;
import com.zzheads.HomeAutomation.config.DataConfig;
import com.zzheads.HomeAutomation.exceptions.DaoException;
import com.zzheads.HomeAutomation.service.RoomService;
import com.zzheads.HomeAutomation.testing.ApiClient;
import com.zzheads.HomeAutomation.testing.ApiResponse;
import org.eclipse.jetty.io.Connection;
import org.hibernate.SessionFactory;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import javax.swing.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

//
// HomeAutomation
// com.zzheads.HomeAutomation.controller created by zzheads on 22.08.2016.
//
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ControlControllerTest {
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private static ApplicationContext appContext;
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private SessionFactory sessionFactory;


    private ApiClient client;
    private Gson gson;

    private static final String PORT = "4568";
    private static final String TEST_DATASOURCE = "jdbc:h2:mem:testing";

    static {
        System.setProperty("properties.home", "E:/Projects/HomeAutomation/propertiesTest/");
    }


    @BeforeClass
    public static void startServer() {
        String[] args = {PORT, TEST_DATASOURCE};
        Application.main(args);
    }

    @AfterClass
    public static void stopServer() {
        SpringApplication.exit(appContext, (ExitCodeGenerator) () -> 0);
    }

    @Before public void setUp() throws Exception {
        client = new ApiClient("http://localhost:8080");
        gson = new Gson();

    }

    @After public void tearDown() throws Exception {
        sessionFactory.close();
    }

    @Test public void testAddControl() throws Exception {
    }

    @Test public void testGetAllControls() throws Exception {

    }

    @Test public void testUpdateControl() throws Exception {

    }

    @Test public void testGetControlById() throws Exception {

    }

    @Test public void testDeleteControlById() throws Exception {

    }

    @Test public void testSetValue() throws Exception {

    }

    @Test public void testGetValue() throws Exception {

    }
}
