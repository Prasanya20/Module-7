package com.cognizant.springlearn.dao;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

import com.cognizant.springlearn.model.Employee;
import com.cognizant.springlearn.service.exception.EmployeeNotFoundException;

@Repository
public class EmployeeDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDao.class);

    @SuppressWarnings("unchecked")
    private static ArrayList<Employee> EMPLOYEE_LIST;

    @SuppressWarnings("unchecked")
    public EmployeeDao() {
        LOGGER.debug("Inside EmployeeDao Constructor.");

        ApplicationContext context = new ClassPathXmlApplicationContext("employee.xml");
        EMPLOYEE_LIST = (ArrayList<Employee>) context.getBean("employeeList");
    }

    public ArrayList<Employee> getAllEmployees() {
        LOGGER.info("START");
        LOGGER.debug("Employee List : {}", EMPLOYEE_LIST);
        LOGGER.info("END");
        return EMPLOYEE_LIST;
    }

    // Replaces the matching employee in the list, or throws if the id isn't found
    public void updateEmployee(Employee employee) throws EmployeeNotFoundException {
        LOGGER.info("START");

        for (int i = 0; i < EMPLOYEE_LIST.size(); i++) {
            if (EMPLOYEE_LIST.get(i).getId().equals(employee.getId())) {
                EMPLOYEE_LIST.set(i, employee);
                LOGGER.debug("Updated Employee : {}", employee);
                LOGGER.info("END");
                return;
            }
        }

        LOGGER.info("END");
        throw new EmployeeNotFoundException("Employee not found");
    }

    // Removes the matching employee from the list, or throws if the id isn't found
    public void deleteEmployee(Integer id) throws EmployeeNotFoundException {
        LOGGER.info("START");

        boolean removed = EMPLOYEE_LIST.removeIf(employee -> employee.getId().equals(id));

        LOGGER.info("END");
        if (!removed) {
            throw new EmployeeNotFoundException("Employee not found");
        }
    }
}
