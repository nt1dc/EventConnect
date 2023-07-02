package com.example.eventconnect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class HealthControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void pingTest() throws Exception {
        MvcResult result = mvc.perform(get("/ping"))
                .andDo(
                        print()
                ).andExpect(status().isOk()).andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        Assertions.assertEquals("pong", contentAsString);
    }
}
