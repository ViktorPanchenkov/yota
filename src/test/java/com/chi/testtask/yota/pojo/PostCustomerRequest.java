package com.chi.testtask.yota.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PostCustomerRequest {


    @JsonProperty("name")
    private String name;
    @JsonProperty("phone")
    private long phone;
    @JsonProperty("string")
    private String string;

}
