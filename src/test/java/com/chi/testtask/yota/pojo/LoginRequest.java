package com.chi.testtask.yota.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class LoginRequest {

    @JsonProperty("login")
    private String login;
    @JsonProperty("password")
    private String password;
}
