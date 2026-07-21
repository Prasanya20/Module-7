package com.cognizant.springlearn.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.springlearn.Country;
import com.cognizant.springlearn.service.CountryService;
import com.cognizant.springlearn.service.exception.CountryNotFoundException;

// REST resource naming convention: base URL at the class level, all methods relative to it
@RestController
@RequestMapping("/countries")
public class CountryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryController.class);

    @Autowired
    private CountryService countryService;

    public CountryController() {
        LOGGER.debug("Inside CountryController Constructor.");
    }

    // GET /countries -> returns all four countries
    @SuppressWarnings("unchecked")
    @GetMapping
    public List<Country> getAllCountries() {
        LOGGER.info("START");

        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        List<Country> countryList = (List<Country>) context.getBean("countryList");

        LOGGER.debug("Country List : {}", countryList);
        LOGGER.info("END");
        return countryList;
    }

    // GET /countries/{code} -> returns a single country matched by code (case-insensitive)
    @GetMapping("/{code}")
    public Country getCountry(@PathVariable String code) throws CountryNotFoundException {
        LOGGER.info("START");

        Country country = countryService.getCountry(code);

        LOGGER.debug("Country : {}", country.toString());
        LOGGER.info("END");
        return country;
    }

    // POST /countries -> creates a country from the request payload.
    // Validation (@Valid) is delegated to GlobalExceptionHandler.handleMethodArgumentNotValid()
    @PostMapping
    public Country addCountry(@RequestBody @Valid Country country) {
        LOGGER.info("START");

        LOGGER.debug("Country : {}", country);

        LOGGER.info("END");
        return country;
    }
}
