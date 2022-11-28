package com.app.employee.config;

import com.app.employee.model.entity.Employee;
import com.app.employee.repository.EmployeeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger logger = LogManager.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {

        return args -> {
            logger.info("Preloading " + repository.save(new Employee(1L, "William", "Smith", "6 - Chief Information Technology Officer", "CEO", "IT", "male", "1 January 1963")));
            logger.info("Preloading " + repository.save(new Employee(2L, "Rezza", "Fredd", "4A - Sr Manager", "Team Lead", "IT", "male", "1 January 1978")));
            logger.info("Preloading " + repository.save(new Employee(3L, "Mohan", "Kumar", "3C - Officer", "Web Developer", "IT", "male", "1 June 1997")));
            logger.info("Preloading " + repository.save(new Employee(4L, "Cindy", "Mac", "3B - Senior Officer", "Senior Developer", "IT", "female", "19 May 1990")));
            logger.info("Preloading " + repository.save(new Employee(5L, "Linda", "Greedman", "2A - Senior Associate", "Quality ", "IT", "female", "11 October 2001")));
        };
    }
}
