package com.chi.testtask.yota.tests;

import com.chi.testtask.yota.helper.LogListener;
import com.chi.testtask.yota.helper.LoginHelper;
import com.chi.testtask.yota.helper.RetryAnalazer;
import com.chi.testtask.yota.dataprovider.CustomerDataDataProvider;
import com.chi.testtask.yota.dataprovider.LoginDataProvider;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import com.chi.testtask.yota.pojo.*;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.hasKey;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Listeners(LogListener.class)
public class UserTests extends BaseTest implements LoginDataProvider, CustomerDataDataProvider {

    String userToken;

    @BeforeClass
    public void PluggingFilters (){

        userToken = LoginHelper.loginAsUser();
    }
    @Description("This test will log in as user")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "ValidUserCredsDataProvider",retryAnalyzer = RetryAnalazer.class)
    public void loginAsUser(String login,String password){
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

        assertThat(responce)
                .extracting(LoginResponce::getToken)
                .isNotEqualTo(" ");
    }
    @Description("This test will log in as invalid user creds")
    @Severity(SeverityLevel.CRITICAL)
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
    @Description("This test will get customer by phone number")
    @Severity(SeverityLevel.CRITICAL)
    @Test(retryAnalyzer = RetryAnalazer.class)
    public void findCustomerByPhone() {

        String data = getRequestBody("findByPhone.xml");
        String body = String.format(data, userToken);
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
    @Description("This test will post customer")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "PostCustomerDataProvider",retryAnalyzer = RetryAnalazer.class)
    public void postCustomer(long phone, String name, String additionalParam){
        PostCustomerRequest request = new PostCustomerRequest();
        request.setPhone(phone);
        request.setName(name);
        request.setString(additionalParam);

        PostCustomerResponce responce =
                given().header("authToken",userToken).
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
    }
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test will post customer with invalid data in request")
    @Test(dataProvider = "InvalidCustomerDataProvider",retryAnalyzer = RetryAnalazer.class)
    public void postCustomerWithInvalidData(long phone, String name, String additionalParam) {
        PostCustomerRequest request = new PostCustomerRequest();
        request.setPhone(phone);
        request.setName(name);
        request.setString(additionalParam);


        PostCustomerResponce responce =
                given().header("authToken", userToken).
                        baseUri(BASE_URL)
                        .basePath("customer/postCustomer")
                        .contentType(ContentType.JSON)
                        .body(request)
                        .post().then().
                        extract().as(PostCustomerResponce.class);
        assertThat(request)
                .extracting(PostCustomerRequest::getName)
                .isNotEqualTo("");
        assertThat(request)
                .extracting(PostCustomerRequest::getString)
                .isNotEqualTo("");

    }


    @Description("This test will change status of customer as user")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dependsOnMethods = "postCustomer",dataProvider = "CustomerStatusDataProvider",retryAnalyzer = RetryAnalazer.class)
    public void changeStatusOfCustomer(String status){
        ChangeCustomerStatusRequest request = new ChangeCustomerStatusRequest();
        request.setCustomerStatus(status);

        ForrbiddenToChangeStatusResponce responce =
                given()
                        .header("authToken", userToken)
                        .baseUri(BASE_URL)
                        .basePath("customer/"+customerID+"/changeCustomerStatus")
                        .contentType(ContentType.JSON)
                        .body(request)
                        .post().then()
                        .statusCode(401)
                .extract().as(ForrbiddenToChangeStatusResponce.class);
        assertThat(responce.getErrorMessage()).isEqualTo("У пользователя не хватает прав на выполнение команды");

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

}
