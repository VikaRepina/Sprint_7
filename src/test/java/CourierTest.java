import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CourierTest {
    private static final String URL = "https://qa-scooter.praktikum-services.ru";
    private static final String Login = "VikaWR";
    private static final String Password = "56123";
    private static final String FirstName = "Test";
    private int courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
    }

    @After
    @Step("Находит id созданного курьера и удаляет его из БД")
    public void tearDown() {
        String requestBodyId = "{ \"login\": \"" + Login + "\", \"password\": \"" + Password + "\" }";
        Response responseId = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBodyId)
                .post("/api/v1/courier/login");
        courierId = responseId.jsonPath().get("id");

        if (courierId > 0) {
            RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .delete("/api/v1/courier/" + courierId)
                    .then()
                    .statusCode(200);
        }
    }

    @Test
    @DisplayName("Test create courier")
    @Description("Проверка на успешное создание курьера")
    public void testCreateCourier() {
        String requestBody = "{ \"login\": \"" + Login + "\", \"password\": \"" + Password + "\", \"firstName\": \"" + FirstName + "\" }";
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/v1/courier");
        response.then().statusCode(201);
        response.then().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Test duplicate create courier")
    @Description("Проверка на вывод ошибки, при создании одинаковых курьеров")
    public void testDuplicateCreateCourier() {
        String requestBody = "{ \"login\": \"" + Login + "\", \"password\": \"" + Password + "\", \"firstName\": \"" + FirstName + "\" }";
        Response createResponse = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/v1/courier");
        if (createResponse.statusCode() == 201) {

        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/v1/courier");
        response.then().statusCode(409);
    }
    }

    @Test
    @DisplayName("Test create courier with existing login")
    @Description("Проверка на вывод ошибки, если создать курьера с уже существующим логином")
    public void testCreateCourierWithExistingLogin() {
        String requestBody = "{ \"login\": \"" + Login + "\", \"password\": \"" + Password + "\", \"firstName\": \"" + FirstName + "\" }";
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/v1/courier");
        response.then().statusCode(201);

        String requestBodySecond = "{ \"login\": \"" + Login + "\", \"password\": \"" + "54789" + "\", \"firstName\": \"" + "Testing" + "\" }";
        Response responseSecond = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBodySecond)
                .post("/api/v1/courier");
        responseSecond.then().statusCode(409);
    }
}
