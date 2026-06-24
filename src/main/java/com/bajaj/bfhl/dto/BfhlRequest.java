package com.bajaj.bfhl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BfhlRequest {

    @NotNull(message = "data field must not be null")
    @Size(min = 1, message = "data array must contain at least one element")
    @JsonProperty("data")
    private List<String> data;
}
