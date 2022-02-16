package com.chi.testtask.yota.tests;

import com.chi.testtask.yota.helper.LogListener;
import com.chi.testtask.yota.helper.LoginHelper;
import com.chi.testtask.yota.helper.RetryAnalazer;
import com.chi.testtask.yota.dataprovider.CustomerDataDataProvider;
import com.chi.testtask.yota.dataprovider.LoginDataProvider;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.chi.testtask.yota.pojo.ChangeCustomerStatusRequest;
import com.chi.testtask.yota.pojo.PostCustomerRequest;
import com.chi.testtask.yota.pojo.PostCustomerResponce;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

@Listeners(LogListener.class)
public class BusinessFlowTests extends BaseTest implements CustomerDataDataProvider, LoginDataProvider {


    String adminToken;
    static List<Long> listOfPhones = new ArrayList<>();
    String iD;


    @BeforeClass
    public void PluggingFilters() {
        RestAssured.filters(new RequestLoggingFilter(LogDetail.METHOD), new RequestLoggingFilter(LogDetail.URI), new RequestLoggingFilter(LogDetail.BODY));
        RestAssured.filters(new ResponseLoggingFilter(LogDetail.STATUS), new ResponseLoggingFilter(LogDetail.BODY));
        adminToken = LoginHelper.loginAsAdmin();
        //userToken = LoginHelper.loginAsUser();

    }
    @Description("This test will get list of free phones")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 1,retryAnalyzer = RetryAnalazer.class)
    public void getListOfNumbers() {
        listOfPhones = given().header("authToken", adminToken)
                .baseUri(BASE_URL)
                .basePath("simcards/getEmptyPhone")
                .when().get().then()
                .assertThat()
                .statusCode(200)
                .body("phones[0]", hasKey("phone"))
                .body("phones[0]", hasKey("locale"))
                .extract().jsonPath().getList("phones.phone");


    }
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 2,dataProvider = "PostCustomerDataProvider", retryAnalyzer = RetryAnalazer.class)
    public void postCustomer(long phone, String name, String additionalParam) {
        PostCustomerRequest request = new PostCustomerRequest();
        request.setPhone(phone);
        request.setName(name);
        request.setString(additionalParam);


        PostCustomerResponce responce =
                given().header("authToken", adminToken).
                        baseUri(BASE_URL)
                        .basePath("customer/postCustomer")
                        .contentType(ContentType.JSON)
                        .body(request)
                        .post().then()
                        .assertThat()
                        .statusCode(200).
                        extract().as(PostCustomerResponce.class);
        assertThat(responce)
                .extracting(PostCustomerResponce::getId)
                .isNotEqualTo("");
        iD = responce.getId();
    }
    @Description("This test will get customer by ID")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 3,dependsOnMethods = "postCustomer", retryAnalyzer = RetryAnalazer.class)
    public void getCustomerByIDs() {

        Response response = given()
                .header("authToken", adminToken)
                .baseUri(BASE_URL)
                .basePath("customer/getCustomerById")
                .param("customerId", iD)
                .contentType(ContentType.JSON)
                .get()
                .then()
                .assertThat().statusCode(200)
                .body("return", hasKey("customerId"))
                .body("return", hasKey("status"))
                .body("return", hasKey("phone"))
                .extract().response();
    }
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 4,dependsOnMethods = "postCustomer")
    public void findCustomerByPhone() {
        String data = getRequestBody("findByPhone.xml");
        String body = String.format(data, adminToken);
        iD = given()
                .baseUri(BASE_URL)
                .basePath("customer/findByPhoneNumber")
                .contentType(ContentType.XML)
                .body(body)
                .post()
                .then().statusCode(200)
                .extract()
                .body()
                .xmlPath().getString("customerId");
        assertThat(iD)
                .isNotEqualTo(" ");
        System.out.println(iD);

    }
    @Description("This test will change status of customer as admin")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 5,dependsOnMethods = "postCustomer", dataProvider = "CustomerStatusDataProvider", retryAnalyzer = RetryAnalazer.class)
    public void changeStatusOfCustomer(String status) {
        ChangeCustomerStatusRequest request = new ChangeCustomerStatusRequest();
        request.setCustomerStatus(status);

        given()
                .header("authToken", adminToken)
                .baseUri(BASE_URL)
                .basePath("customer/"+iD+"/changeCustomerStatus")
                .contentType(ContentType.JSON)
                .body(request)
                .post().then()
                .statusCode(200);

        Response responce
                = given()
                .header("authToken", adminToken)
                .baseUri(BASE_URL)
                .basePath("customer/getCustomerById")
                .param("customerId", iD)
                .contentType(ContentType.JSON)
                .get()
                .then()
                .assertThat().statusCode(200)
                .body("return.status", equalTo(status))
                .extract().response();
    }

}
