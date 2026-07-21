package com.cognizant.springlearn.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.springlearn.dao.DepartmentDao;
import com.cognizant.springlearn.model.Department;

@Service
public class DepartmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentService.class);

    @Autowired
    private DepartmentDao departmentDao;

    public DepartmentService() {
        LOGGER.debug("Inside DepartmentService Constructor.");
    }

    public List<Department> getAllDepartments() {
        LOGGER.info("START");

        List<Department> departments = departmentDao.getAllDepartments();

        LOGGER.debug("Department List : {}", departments);
        LOGGER.info("END");
        return departments;
    }
}
