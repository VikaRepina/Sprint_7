import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class CourierSecondTest {
    private static final String URL = "https://qa-scooter.praktikum-services.ru";
    private Integer courierId;

    private String login;
    private String password;
    private String firstName;

    public CourierSecondTest(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Before
    public void setup() {
        RestAssured.baseURI = URL;
    }


    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"", "", "Test"},
                {"VikaWRI", "", "Test"},
                {"", "56122", "Test"},
                {null, null, "Test"},
                {"VikaWRI", null, "Test"},
                {null, "56122", "Test"},
        });
    }

    @Test
    @DisplayName("Test create courier with missing field")
    @Description("Проверка на возврат ошибки, когда созадаешь курьера, при пустом поле или его отсутствие")
    public void testCreateCourierWithMissingField() {
        String requestBody = String.format("{\"login\": \"%s\", \"password\": \"%s\", \"firstName\": \"%s\" }",
                login != null ? login : "",
                password != null ? password : "",
                firstName != null ? firstName : "");
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/v1/courier");
        response.then().statusCode(400);

    }
}
