package com.bajaj.bfhl.controller;

import com.bajaj.bfhl.dto.BfhlResponse;
import com.bajaj.bfhl.service.BfhlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BfhlController.class)
class BfhlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BfhlService bfhlService;

    @Test
    @DisplayName("POST /bfhl → 200 OK with all fields")
    void postBfhl_success() throws Exception {
        when(bfhlService.processData(any())).thenReturn(buildMockResponse());

        String body = objectMapper.writeValueAsString(Map.of("data", List.of("a", "1", "334", "4", "R", "$")));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.user_id").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.roll_number").exists())
                .andExpect(jsonPath("$.odd_numbers").isArray())
                .andExpect(jsonPath("$.even_numbers").isArray())
                .andExpect(jsonPath("$.alphabets").isArray())
                .andExpect(jsonPath("$.special_characters").isArray())
                .andExpect(jsonPath("$.sum").exists())
                .andExpect(jsonPath("$.concat_string").exists());
    }

    @Test
    @DisplayName("POST /bfhl missing data field → 400")
    void postBfhl_missingDataField_returns400() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    @Test
    @DisplayName("POST /bfhl malformed JSON → 400")
    void postBfhl_malformedJson_returns400() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("not-valid-json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    @Test
    @DisplayName("GET /bfhl → 200 with operation_code")
    void getBfhl_returnsOperationCode() throws Exception {
        mockMvc.perform(get("/bfhl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation_code").value(1));
    }

    private BfhlResponse buildMockResponse() {
        return BfhlResponse.builder()
                .isSuccess(true)
                .userId("aniket_singla_01102005")
                .email("aniket2100.be23@chitkara.edu.in")
                .rollNumber("2310992100")
                .oddNumbers(List.of("1"))
                .evenNumbers(List.of("334", "4"))
                .alphabets(List.of("A", "R"))
                .specialCharacters(List.of("$"))
                .sum("339")
                .concatString("Ra")
                .build();
    }
}
