package vn.binhduong.eventmgrdemo.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by binhduong on 11/7/2021.
 */
@SpringBootTest
@AutoConfigureMockMvc
class EventsEntitySimpleControllerTest {

    private static final String END_POINT = "http://localhost:8080/api/simple/events";

    @Autowired
    private MockMvc mockMvc;

    /**
     * Given:
     * 1. Target endpoint {http://localhost:8080/api/simple/events} is used.
     * <p>
     * When:
     * 1. The endpoint is called by using the GET method
     * <p>
     * Then expect:
     * 1. Client will receive the list of 7 events in json format (the data was initialized when the app boot up)
     *
     * @throws Exception
     */
    @Test
    void query_Given_07EventsExisted_Then_ReturnAll07Events() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)));
    }
}