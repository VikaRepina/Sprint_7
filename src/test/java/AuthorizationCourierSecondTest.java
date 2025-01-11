import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class AuthorizationCourierSecondTest {
    private static final String URL = "https://qa-scooter.praktikum-services.ru";

    private String loginA;
    private String passwordA;

    public AuthorizationCourierSecondTest(String loginA,String passwordA) {
        this.loginA = loginA;
        this.passwordA = passwordA;
    }

    @Before
    public void setup() {
        RestAssured.baseURI = URL;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"", ""},
                {"VikaWRQ", ""},
                {"", "52413"},
                {null, null},
                {"VikaWRQ", null},
                {null, "52413"},
        });
    }

    @Test
    @DisplayName("Test courier with missing field")
    @Description("Проверка на вывод ошибки, при пустом поле или его отсутствие в процессе авторизации")
    public void testCourierWithMissingField() {
        String requestBody = String.format("{\"login\": \"%s\", \"password\": \"%s\"}",
                loginA != null ? loginA : "",
                passwordA != null ? passwordA : "");
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/v1/courier/login");
        response.then().statusCode(400);
    }
}
