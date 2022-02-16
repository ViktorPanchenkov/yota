package com.chi.testtask.yota.tests;

import com.chi.testtask.yota.helper.LogListener;
import com.chi.testtask.yota.helper.LoginHelper;
import com.chi.testtask.yota.dataprovider.CustomerDataDataProvider;
import com.chi.testtask.yota.dataprovider.LoginDataProvider;
import com.chi.testtask.yota.helper.RetryAnalazer;
import com.chi.testtask.yota.pojo.*;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

@Listeners(LogListener.class)
public class AdminTests extends BaseTest implements LoginDataProvider, CustomerDataDataProvider {

    String adminToken;
    String customerID;


    @BeforeClass
    public void PluggingFilters (){



        adminToken = LoginHelper.loginAsAdmin();

    }
    @Description("This test will login as admin")
    @Severity(SeverityLevel.BLOCKER)
    @Test(dataProvider = "ValidAdminCredsDataProvider",retryAnalyzer = RetryAnalazer.class)
    public void loginAsAdmin(String login, String password){
        LoginRequest request = new LoginRequest();
        request.setLogin(login);
        request.setPassword(password);

        LoginResponce responce =
                given()
                .baseUri(BASE_URL)
                .basePath("login")
                .contentType(ContentType.JSON)
                .body(request)
                .when().post()
                .then().
                        assertThat()
                .statusCode(200)
                .extract().as(LoginResponce.class);

        assertThat(responce).isNotNull()
                .extracting(LoginResponce::getToken)
                .isNotEqualTo(" ");
    }
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test will log in with invalid creds as admin")
    @Test(dataProvider = "InValidCredsDataProvider",retryAnalyzer = RetryAnalazer.class)
    public void loginWithInvalidCreds(String login, String password){
        LoginRequest request = new LoginRequest();
        request.setLogin(login);
        request.setPassword(password);

        InvalidCredsLoginResponce responce =
                given()
                        .baseUri(BASE_URL)
                        .basePath("login")
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when().post()
                        .then().
                        assertThat()
        .statusCode(400)
                        .extract().as(InvalidCredsLoginResponce.class);
        assertThat(responce)
                .extracting(InvalidCredsLoginResponce::getErrorMessage)
                .isEqualTo("Не корректный логин/пароль");

    }
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test will get list of free numbers")
    @Test(retryAnalyzer = RetryAnalazer.class)
    public void getListOfNumbers(){

        Response response =
                given().header("authToken",adminToken)
                        .baseUri(BASE_URL)
                .basePath("simcards/getEmptyPhone")
                .when().get().then()
                        .assertThat()
                        .statusCode(200)
                .body("phones[0]",hasKey("phone"))
                .body("phones[0]",hasKey("locale"))
                .extract().response();

    }
    @Description("This test will post customer")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "PostCustomerDataProvider",retryAnalyzer = RetryAnalazer.class)
    public void postCustomer(long phone, String name, String additionalParam){
        adminToken = LoginHelper.loginAsAdmin();
        PostCustomerRequest request = new PostCustomerRequest();
        request.setPhone(phone);
        request.setName(name);
        request.setString(additionalParam);

        PostCustomerResponce responce =
                given().header("authToken",adminToken).
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

        customerID = responce.getId();
    }
    @Description("This test will get customer by phone number")
    @Test
    public void findCustomerByPhone() {

        String data = getRequestBody("findByPhone.xml");
        String body = String.format(data, adminToken);
       String id = given()
                .baseUri(BASE_URL)
                .basePath("customer/findByPhoneNumber")
                .contentType(ContentType.XML)
                .body(body)
                .post()
                .then().statusCode(200)
                .extract()
                .body()
                .xmlPath().getString("customerId");
         assertThat(id)
                 .isNotEqualTo(" ");

    }
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test will post customer with invalid data")
    @Test(dataProvider = "InvalidCustomerDataProvider",retryAnalyzer = RetryAnalazer.class)
    public void postCustomerWithInvalidData(long phone, String name, String additionalParam) {
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
                        .assertThat().
                        extract().as(PostCustomerResponce.class);
        assertThat(request)
                .extracting(PostCustomerRequest::getName)
                .isNotEqualTo("");
        assertThat(request)
                .extracting(PostCustomerRequest::getString)
                .isNotEqualTo("");
    }
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test will get customer by ID")
    @Test(dependsOnMethods = "postCustomer",retryAnalyzer = RetryAnalazer.class)
    public void getCustomerByID(){

        Response response = given()
                .header("authToken",adminToken)
                .baseUri(BASE_URL)
                .basePath("customer/getCustomerById")
                .param("customerId",customerID)
                .contentType(ContentType.JSON)
                .get()
                .then()
                .assertThat().statusCode(200)
                .body("return", hasKey("customerId"))
                .body("return", hasKey("status"))
                .body("return",hasKey("phone"))
                .extract().response();
    }
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test will send request with invalid token")
    @Test
    public void getCustomerByIDWithInvalidToken(){
        InvalidTokenResponce response = given()
                .header("authToken","dsfsdfsdfsdfsdfds")
                .baseUri(BASE_URL)
                .basePath("customer/getCustomerById")
                .param("customerId",customerID)
                .contentType(ContentType.JSON)
                .get()
                .then()
                .assertThat().statusCode(401)
                .extract().as(InvalidTokenResponce.class);
        assertThat(response.getErrorMessage())
                .isEqualTo("Не корректный токен пользователя");
    }
    @Description("This test will change status of customer as admin")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dependsOnMethods = "postCustomer", dataProvider = "CustomerStatusDataProvider", retryAnalyzer = RetryAnalazer.class)
    public void changeStatusOfCustomer(String status) {
        ChangeCustomerStatusRequest request = new ChangeCustomerStatusRequest();
        request.setCustomerStatus(status);

        given()
                .header("authToken", adminToken)
                .baseUri(BASE_URL)
                .basePath("customer/"+customerID+"/changeCustomerStatus")
                .contentType(ContentType.JSON)
                .body(request)
                .post().then()
                .statusCode(200);

        Response responce
                = given()
                .header("authToken", adminToken)
                .baseUri(BASE_URL)
                .basePath("customer/getCustomerById")
                .param("customerId", customerID)
                .contentType(ContentType.JSON)
                .get()
                .then()
                .assertThat().statusCode(200)
                .body("return.status", equalTo(status))
                .extract().response();
    }

}
