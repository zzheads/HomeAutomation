package com.zzheads.HomeAutomation;

// HomeAutomation
// com.zzheads.HomeAutomation created by zzheads on 19.08.2016.
//

// DONE:     In the IDE of your choice, create a Gradle project.
// DONE:     Add dependencies for Spring Boot, Spring ORM, JUnit, Hibernate, Apache DBCP, and H2.
// DONE:     Create the directory and package structure of the application. Save all static assets into the proper directory.
// DONE:     Create application configuration class, including methods for initializing session factory and data source.
// DONE:     Create the configuration files for Hibernate and an H2 database connection.
// DONE:     Using the API documentation, create model classes that represent the API resources and add JPA annotations.
// DONE:     Create a Spring Data DAO interface for each model class.
// DONE:     Create a service interface and implementation for each model class.
// DONE:     Create controllers for all endpoints in documentation, using appropriate annotations.
// TODO:     Write unit tests for all controllers, services, and DAOs.
//
//
//      Extra Credit
//
// TODO:     Use a third party mocking library, such as Mockito, for unit testing.
// DONE:     Use @ExceptionHandler annotations and @ResponseStatus annotations for exception handling.
// DONE:     Create a custom endpoint for retrieving all rooms, equipment, controls, and values.


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class Application {

    public static final String BASE_URL ="http://localhost:8080/";
    private static ConfigurableApplicationContext appContext;

    static {
        System.setProperty("properties.home", "E:/Projects/HomeAutomation/properties/");
    }

    public static void main(String[] args) {
        appContext = SpringApplication.run(Application.class, args);
    }

    public static ConfigurableApplicationContext getAppContext() {
        return appContext;
    }

    public static void setAppContext(ConfigurableApplicationContext appContext) {
        Application.appContext = appContext;
    }
}
