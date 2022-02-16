package com.chi.testtask.yota.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ChangeCustomerStatusRequest {

    @JsonProperty("status")
    private String customerStatus;
}