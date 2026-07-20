package com.cognizant.springlearn.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Employee {

    private static final Logger LOGGER = LoggerFactory.getLogger(Employee.class);

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Department department;

    public Employee() {
        LOGGER.debug("Inside Employee Constructor.");
    }

    public int getId() {
        LOGGER.debug("Inside getId()");
        return id;
    }

    public void setId(int id) {
        LOGGER.debug("Inside setId(), value : {}", id);
        this.id = id;
    }

    public String getFirstName() {
        LOGGER.debug("Inside getFirstName()");
        return firstName;
    }

    public void setFirstName(String firstName) {
        LOGGER.debug("Inside setFirstName(), value : {}", firstName);
        this.firstName = firstName;
    }

    public String getLastName() {
        LOGGER.debug("Inside getLastName()");
        return lastName;
    }

    public void setLastName(String lastName) {
        LOGGER.debug("Inside setLastName(), value : {}", lastName);
        this.lastName = lastName;
    }

    public String getEmail() {
        LOGGER.debug("Inside getEmail()");
        return email;
    }

    public void setEmail(String email) {
        LOGGER.debug("Inside setEmail(), value : {}", email);
        this.email = email;
    }

    public String getPhone() {
        LOGGER.debug("Inside getPhone()");
        return phone;
    }

    public void setPhone(String phone) {
        LOGGER.debug("Inside setPhone(), value : {}", phone);
        this.phone = phone;
    }

    public Department getDepartment() {
        LOGGER.debug("Inside getDepartment()");
        return department;
    }

    public void setDepartment(Department department) {
        LOGGER.debug("Inside setDepartment(), value : {}", department);
        this.department = department;
    }

    @Override
    public String toString() {
        return "Employee [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName
                + ", email=" + email + ", phone=" + phone + ", department=" + department + "]";
    }
}
