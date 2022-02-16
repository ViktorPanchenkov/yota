package com.chi.testtask.yota.helper;

import com.chi.testtask.yota.dataprovider.LoginDataProvider;
import com.chi.testtask.yota.pojo.LoginRequest;
import com.chi.testtask.yota.tests.BaseTest;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;


public class LoginHelper implements LoginDataProvider {


    @Step
    public static String loginAsAdmin(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("admin");
        loginRequest.setPassword("password");

        String adminToken =   given()
                .baseUri(BaseTest.BASE_URL)
                .basePath("login")
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when().post()
                .then().statusCode(200)
                .extract().path("token");
        return adminToken;

    }
    @Step
    public static String loginAsUser(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("user");
        loginRequest.setPassword("password");

        String userToken =   given()
                .baseUri(BaseTest.BASE_URL)
                .basePath("login")
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when().post()
                .then().statusCode(200)
                .extract().path("token");
        return userToken;

    }


}
