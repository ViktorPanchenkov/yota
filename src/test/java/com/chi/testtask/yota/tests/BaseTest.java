package com.chi.testtask.yota.tests;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class BaseTest {
    public static final String BASE_URL = "http://localhost:8080/";

    public String getRequestBody(String name){
        String data = null;
        try {
            data = new String(Files.readAllBytes(Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(name)).toURI())));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return data;
    }



}
