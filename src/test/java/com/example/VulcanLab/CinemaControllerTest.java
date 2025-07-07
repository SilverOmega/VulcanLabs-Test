package com.example.VulcanLab;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import service.CinemaService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
public class CinemaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CinemaService cinemaService;

    @BeforeEach
    void setup() {
        cinemaService.configure(5, 5, 6);
    }

    @Test
    void testConfigure() throws Exception {
        mockMvc.perform(post("/cinema/configure")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rows\": 5, \"cols\": 5, \"min_distance\": 6}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cinema configured."));
    }

    @Test
    void testAvailableSeats() throws Exception {
        mockMvc.perform(get("/cinema/available-seats")
                        .param("groupSize", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0][0].row").exists())
                .andExpect(jsonPath("$[0][0].col").exists());
    }

    @Test
    void testReserveAndCancel() throws Exception {
        mockMvc.perform(post("/cinema/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"row\":0,\"col\":0},{\"row\":0,\"col\":1}]"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reserved."));

        mockMvc.perform(post("/cinema/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"row\":0,\"col\":0},{\"row\":0,\"col\":1}]"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cancelled."));
    }

    @Test
    void testReserveConflict() throws Exception {
        mockMvc.perform(post("/cinema/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"row\":0,\"col\":0}]"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/cinema/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"row\":1,\"col\":0}]"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to reserve."));
    }
}
