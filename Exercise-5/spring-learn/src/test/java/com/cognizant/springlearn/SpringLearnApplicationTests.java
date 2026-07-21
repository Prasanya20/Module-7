package com.cognizant.springlearn;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.cognizant.springlearn.controller.CountryController;
import com.cognizant.springlearn.controller.EmployeeController;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    // --- Spring Security: HTTP Basic tests ---

    @Test
    void testGetCountriesWithoutAuthIsUnauthorized() throws Exception {
        mvc.perform(get("/countries")).andExpect(status().isUnauthorized());
    }

    @Test
    void testGetCountriesWithUserCredentials() throws Exception {
        mvc.perform(get("/countries").with(httpBasic("user", "pwd")))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCountriesWithWrongPassword() throws Exception {
        mvc.perform(get("/countries").with(httpBasic("user", "wrongpwd")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetCountry() throws Exception {
        ResultActions actions = mvc.perform(get("/countries/in").with(httpBasic("user", "pwd")));
        actions.andExpect(status().isOk());
        actions.andExpect(jsonPath("$.code").value("IN"));
        actions.andExpect(jsonPath("$.name").value("India"));
    }

    @Test
    void testGetCountryException() throws Exception {
        ResultActions actions = mvc.perform(get("/countries/az").with(httpBasic("user", "pwd")));
        actions.andExpect(status().isNotFound());
        actions.andExpect(status().reason("Country not found"));
    }

    @Test
    void testAddCountryValidationError() throws Exception {
        ResultActions actions = mvc.perform(post("/countries")
                .with(httpBasic("user", "pwd"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"code\":\"I\",\"name\":\"India\"}"));
        actions.andExpect(status().isBadRequest());
        actions.andExpect(jsonPath("$.errors[0]").value("Country code should be 2 characters"));
    }

    @Test
    void testUpdateEmployeeNotFound() throws Exception {
        String payload = "{\"id\":999,\"firstName\":\"Test\",\"lastName\":\"User\","
                + "\"salary\":50000,\"permanent\":true,\"dateOfBirth\":\"01/01/2000\"}";

        ResultActions actions = mvc.perform(put("/employees")
                .with(httpBasic("user", "pwd"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload));
        actions.andExpect(status().isNotFound());
        actions.andExpect(status().reason("Employee not found"));
    }

    @Test
    void testDeleteEmployeeNotFound() throws Exception {
        ResultActions actions = mvc.perform(delete("/employees/999").with(httpBasic("user", "pwd")));
        actions.andExpect(status().isNotFound());
        actions.andExpect(status().reason("Employee not found"));
    }

    // --- JWT tests ---

    @Test
    void testAuthenticateReturnsToken() throws Exception {
        MvcResult result = mvc.perform(get("/authenticate").with(httpBasic("user", "pwd")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        String token = new ObjectMapper().readTree(body).get("token").asText();
        assertTrue(token != null && !token.isEmpty());
    }

    @Test
    void testAccessProtectedEndpointWithJwt() throws Exception {
        // First authenticate to obtain a token
        MvcResult authResult = mvc.perform(get("/authenticate").with(httpBasic("user", "pwd")))
                .andExpect(status().isOk())
                .andReturn();
        String body = authResult.getResponse().getContentAsString();
        String token = new ObjectMapper().readTree(body).get("token").asText();

        // Then use the token as a Bearer header instead of HTTP Basic
        mvc.perform(get("/countries").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void testAccessProtectedEndpointWithInvalidJwtIsUnauthorized() throws Exception {
        mvc.perform(get("/countries").header("Authorization", "Bearer invalid.token.value"))
                .andExpect(status().isUnauthorized());
    }
}
