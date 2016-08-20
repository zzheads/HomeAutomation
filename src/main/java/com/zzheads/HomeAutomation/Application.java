package com.zzheads.HomeAutomation;

// HomeAutomation
// com.zzheads.HomeAutomation created by zzheads on 19.08.2016.
//

// TODO:     In the IDE of your choice, create a Gradle project.
// TODO:     Add dependencies for Spring Boot, Spring ORM, JUnit, Hibernate, Apache DBCP, and H2.
// TODO:     Create the directory and package structure of the application. Save all static assets into the proper directory.
// TODO:     Create application configuration class, including methods for initializing session factory and data source.
// TODO:     Create the configuration files for Hibernate and an H2 database connection.
// TODO:     Using the API documentation, create model classes that represent the API resources and add JPA annotations.
// TODO:     Create a Spring Data DAO interface for each model class.
// TODO:     Create a service interface and implementation for each model class.
// TODO:     Create controllers for all endpoints in documentation, using appropriate annotations.
// TODO:     Write unit tests for all controllers, services, and DAOs.
//
//
//      Extra Credit
//
// TODO:     Use a third party mocking library, such as Mockito, for unit testing.
// TODO:     Use @ExceptionHandler annotations and @ResponseStatus annotations for exception handling.
// TODO:     Create a custom endpoint for retrieving all rooms, equipment, controls, and values.


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
