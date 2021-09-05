package com.miw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miw.database.JdbcClientDao;
import com.miw.model.Credentials;
import com.miw.service.authentication.AuthenticationService;
import com.miw.service.authentication.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

    private Logger logger = LoggerFactory.getLogger(LoginControllerTest.class);
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private JdbcClientDao jdbcClientDao;

    @Autowired
    public LoginControllerTest(MockMvc mockMvc) {
        super();
        this.mockMvc = mockMvc;
        logger.info("New LoginController Test");
    }

    @Test
    void loginUser() {
        Credentials validCredentials = new Credentials("test@test.com", "zeerveiligwachtwoord2");
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MzA2OTE4ODEsInN1YiI6InRlc3RAdGVzdC5jb20iLCJleHAiOjE2MzA2OTkyODF9.e0FE2OrTRImfnkIKkkuACikdh-CZHq7KuzTnQYeUsOI";
        Mockito.when(authenticationService.authenticate(validCredentials)).thenReturn(token);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/login");
        request.contentType(MediaType.APPLICATION_JSON).content(asJsonString(validCredentials));
        try {
            ResultActions actions = mockMvc.perform(request);
            MockHttpServletResponse response = actions.andExpect(status().isOk()).andDo(print()).andReturn().getResponse();
            assertThat(response.getContentAsString()).isNotEmpty();
            assertThat(response.getContentType()).isEqualTo("text/plain;charset=UTF-8");
            assertThat(response.getContentAsString()).isEqualTo(token);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}