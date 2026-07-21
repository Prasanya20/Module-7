package com.cognizant.springlearn.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Employee {

    private static final Logger LOGGER = LoggerFactory.getLogger(Employee.class);

    @NotNull
    private Integer id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 30)
    private String firstName;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 30)
    private String lastName;

    private String email;
    private String phone;

    @NotNull
    @Min(0)
    private Double salary;

    @NotNull
    private Boolean permanent;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private java.util.Date dateOfBirth;

    @Valid
    private Department department;

    @Valid
    private List<Skill> skills;

    public Employee() {
        LOGGER.debug("Inside Employee Constructor.");
    }

    public Integer getId() {
        LOGGER.debug("Inside getId()");
        return id;
    }

    public void setId(Integer id) {
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

    public Double getSalary() {
        LOGGER.debug("Inside getSalary()");
        return salary;
    }

    public void setSalary(Double salary) {
        LOGGER.debug("Inside setSalary(), value : {}", salary);
        this.salary = salary;
    }

    public Boolean getPermanent() {
        LOGGER.debug("Inside getPermanent()");
        return permanent;
    }

    public void setPermanent(Boolean permanent) {
        LOGGER.debug("Inside setPermanent(), value : {}", permanent);
        this.permanent = permanent;
    }

    public java.util.Date getDateOfBirth() {
        LOGGER.debug("Inside getDateOfBirth()");
        return dateOfBirth;
    }

    public void setDateOfBirth(java.util.Date dateOfBirth) {
        LOGGER.debug("Inside setDateOfBirth(), value : {}", dateOfBirth);
        this.dateOfBirth = dateOfBirth;
    }

    public Department getDepartment() {
        LOGGER.debug("Inside getDepartment()");
        return department;
    }

    public void setDepartment(Department department) {
        LOGGER.debug("Inside setDepartment(), value : {}", department);
        this.department = department;
    }

    public List<Skill> getSkills() {
        LOGGER.debug("Inside getSkills()");
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        LOGGER.debug("Inside setSkills(), value : {}", skills);
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "Employee [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName
                + ", email=" + email + ", phone=" + phone + ", salary=" + salary
                + ", permanent=" + permanent + ", dateOfBirth=" + dateOfBirth
                + ", department=" + department + ", skills=" + skills + "]";
    }
}
