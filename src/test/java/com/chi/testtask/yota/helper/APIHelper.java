package com.chi.testtask.yota.helper;

import com.chi.testtask.yota.dataprovider.LoginDataProvider;
import com.chi.testtask.yota.tests.BaseTest;
import io.restassured.http.ContentType;
import com.chi.testtask.yota.pojo.PostCustomerRequest;
import com.chi.testtask.yota.pojo.PostCustomerResponce;

import static io.restassured.RestAssured.given;

public class APIHelper implements LoginDataProvider {

    public void postUser(long phone,String name,String additionalParam, String adminToken){

        PostCustomerRequest request = new PostCustomerRequest();
        request.setPhone(phone);
        request.setName(name);
        request.setString(additionalParam);

        PostCustomerResponce responce =
                given().header("authToken",adminToken).
                        baseUri(BaseTest.BASE_URL)
                        .basePath("customer/postCustomer")
                        .contentType(ContentType.JSON)
                        .body(request)
                        .post().then()
                        .assertThat()
                        .statusCode(200).
                        extract().as(PostCustomerResponce.class);
    }


}
