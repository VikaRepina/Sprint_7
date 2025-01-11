import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class AuthorizationCourierTest {
    private static final String URL = "https://qa-scooter.praktikum-services.ru";
    private static final String LoginA = "VikaWRQ";
    private static final String PasswordA = "52413";
    private static final String LoginN = "VikaQ";
    private static final String PasswordN = "58913";

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
    }

    @Test
    @DisplayName("Test successful authorization courier")
    @Description("Проверка на успешную авторизацию курьера")
    public void testSuccessfulAuthorizationCourier() {
        String requestBody = "{ \"login\": \"" + LoginA + "\", \"password\": \"" + PasswordA + "\" }";
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/v1/courier/login");
        response.then().statusCode(200);
        response.then().body("id", notNullValue());

    }

    @Test
    @DisplayName("Test authorization nonexistent courier")
    @Description("Проверка на вывод ошибки, при авторизации под несуществующим пользователем")
    public void testAuthorizationNonexistentCourier() {
        String requestBody = "{ \"login\": \"" + LoginN + "\", \"password\": \"" + PasswordN + "\" }";
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/v1/courier/login");
        response.then().statusCode(404);
        response.then().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Test invalid login")
    @Description("Проверка на вывод ошибки, при авторизации под неправильно указанным логином")
    public void testInvalidLogin () {
        String requestBody = "{ \"login\": \"" + LoginN + "\", \"password\": \"" + PasswordA + "\" }";
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/v1/courier/login");
        response.then().statusCode(404);
    }

    @Test
    @DisplayName("Test invalid password")
    @Description("Проверка на вывод ошибки, при авторизации под неправильно указанным паролем")
    public void testInvalidPassword() {
        String requestBody = "{ \"login\": \"" + LoginA + "\", \"password\": \"" + PasswordN + "\" }";
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/v1/courier/login");
        response.then().statusCode(404);
    }
}
