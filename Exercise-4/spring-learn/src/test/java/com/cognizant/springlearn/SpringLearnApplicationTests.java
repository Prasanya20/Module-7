package com.cognizant.springlearn;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.cognizant.springlearn.controller.CountryController;
import com.cognizant.springlearn.controller.EmployeeController;

@SpringBootTest
@AutoConfigureMockMvc
class SpringLearnApplicationTests {

    @Autowired
    private CountryController countryController;

    @Autowired
    private EmployeeController employeeController;

    @Autowired
    private MockMvc mvc;

    @Test
    void contextLoads() {
        assertNotNull(countryController);
        assertNotNull(employeeController);
    }

    @Test
    void testGetCountry() throws Exception {
        ResultActions actions = mvc.perform(get("/countries/in"));
        actions.andExpect(status().isOk());
        actions.andExpect(jsonPath("$.code").value("IN"));
        actions.andExpect(jsonPath("$.name").value("India"));
    }

    // Note: the earlier doc's snippet checks status().isBadRequest() / reason("Country Not found"),
    // but CountryNotFoundException's @ResponseStatus is HttpStatus.NOT_FOUND, reason "Country not found".
    @Test
    void testGetCountryException() throws Exception {
        ResultActions actions = mvc.perform(get("/countries/az"));
        actions.andExpect(status().isNotFound());
        actions.andExpect(status().reason("Country not found"));
    }

    @Test
    void testAddCountryValidationError() throws Exception {
        ResultActions actions = mvc.perform(post("/countries")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"code\":\"I\",\"name\":\"India\"}"));
        actions.andExpect(status().isBadRequest());
        actions.andExpect(jsonPath("$.errors[0]").value("Country code should be 2 characters"));
    }

    // Exceptional scenario for Employee update: unknown id should return 404 via
    // EmployeeNotFoundException's @ResponseStatus
    @Test
    void testUpdateEmployeeNotFound() throws Exception {
        String payload = "{\"id\":999,\"firstName\":\"Test\",\"lastName\":\"User\","
                + "\"salary\":50000,\"permanent\":true,\"dateOfBirth\":\"01/01/2000\"}";

        ResultActions actions = mvc.perform(put("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload));
        actions.andExpect(status().isNotFound());
        actions.andExpect(status().reason("Employee not found"));
    }

    // Sending a non-numeric id triggers handleHttpMessageNotReadable() in GlobalExceptionHandler
    @Test
    void testUpdateEmployeeInvalidIdFormat() throws Exception {
        String payload = "{\"id\":\"abc\",\"firstName\":\"Test\",\"lastName\":\"User\","
                + "\"salary\":50000,\"permanent\":true,\"dateOfBirth\":\"01/01/2000\"}";

        ResultActions actions = mvc.perform(put("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload));
        actions.andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteEmployeeNotFound() throws Exception {
        ResultActions actions = mvc.perform(delete("/employees/999"));
        actions.andExpect(status().isNotFound());
        actions.andExpect(status().reason("Employee not found"));
    }
}
