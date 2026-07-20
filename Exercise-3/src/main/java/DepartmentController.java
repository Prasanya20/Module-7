package com.cognizant.springlearn.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.springlearn.model.Department;
import com.cognizant.springlearn.service.DepartmentService;

@RestController
public class DepartmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private DepartmentService departmentService;

    public DepartmentController() {
        LOGGER.debug("Inside DepartmentController Constructor.");
    }

    @GetMapping("/departments")
    public List<Department> getAllDepartments() {
        LOGGER.info("START");

        List<Department> departments = departmentService.getAllDepartments();

        LOGGER.debug("Department List : {}", departments);
        LOGGER.info("END");
        return departments;
    }
}
