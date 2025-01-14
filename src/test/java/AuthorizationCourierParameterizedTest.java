import com.google.gson.Gson;
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

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class AuthorizationCourierParameterizedTest {
    private static final String URL = "https://qa-scooter.praktikum-services.ru";
    private String loginA;
    private String passwordA;
    private static final Gson gson = new Gson();

    public AuthorizationCourierParameterizedTest(String loginA, String passwordA) {
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
        });
    }

    @Test
    @DisplayName("Test courier with missing field")
    @Description("Проверка на вывод ошибки, при пустом поле или его отсутствие в процессе авторизации")
    public void testCourierWithMissingField() {
        CourierApi courierApi = new CourierApi();
        CourierLoginRequest request = new CourierLoginRequest(loginA, passwordA);
        Response response = courierApi.AuthorizationCourier(request);
        response.then().statusCode(400);
        response.then().body("message", equalTo("Недостаточно данных для входа"));
    }
}
