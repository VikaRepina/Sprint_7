import com.google.gson.Gson;
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
import static org.hamcrest.CoreMatchers.notNullValue;

public class AuthorizationCourierTest {
    private static final String LoginA = "VikaWRQS";
    private static final String PasswordA = "52763";
    private static final String FirstNameA = "Test";
    private static final String LoginN = "VikaQ";
    private static final String PasswordN = "58913";
    private int courierId;
    private final Gson gson = new Gson();

    @Before
    public void setUp() {
        CourierApi courierApi = new CourierApi();
        Courier courier = new Courier(LoginA, PasswordA, FirstNameA);
        Response response = courierApi.createCourier(courier);
        response.then().statusCode(201);
    }

    @After
    @Step("Находит id созданного курьера и удаляет его из БД")
    public void tearDown() {
        CourierApi courierApi = new CourierApi();
        CourierLoginRequest loginRequest = new CourierLoginRequest(LoginA, PasswordA);
        Response response = courierApi.deleteCourier(courierId, loginRequest);

        if (response != null) {
            response.then().statusCode(200);
        }
    }


    @Test
    @DisplayName("Test successful authorization courier")
    @Description("Проверка на успешную авторизацию курьера")
    public void testSuccessfulAuthorizationCourier() {
        CourierApi courierApi = new CourierApi();
        CourierLoginRequest request = new CourierLoginRequest(LoginA, PasswordA);
        Response response = courierApi.authorizationCourier(request);
        response.then().statusCode(200);
        response.then().body("id", notNullValue());
    }

    @Test
    @DisplayName("Test authorization nonexistent courier")
    @Description("Проверка на вывод ошибки, при авторизации под несуществующим пользователем")
    public void testAuthorizationNonexistentCourier() {
        CourierApi courierApi = new CourierApi();
        CourierLoginRequest request = new CourierLoginRequest(LoginN, PasswordN);
        Response response = courierApi.authorizationCourier(request);
        response.then().statusCode(404);
        response.then().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Test invalid login")
    @Description("Проверка на вывод ошибки, при авторизации под неправильно указанным логином")
    public void testInvalidLogin() {
        CourierApi courierApi = new CourierApi();
        CourierLoginRequest request = new CourierLoginRequest(LoginN, PasswordA);
        Response response = courierApi.authorizationCourier(request);
        response.then().statusCode(404);
        response.then().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Test invalid password")
    @Description("Проверка на вывод ошибки, при авторизации под неправильно указанным паролем")
    public void testInvalidPassword() {
        CourierApi courierApi = new CourierApi();
        CourierLoginRequest request = new CourierLoginRequest(LoginA, PasswordN);
        Response response = courierApi.authorizationCourier(request);
        response.then().statusCode(404);
        response.then().body("message", equalTo("Учетная запись не найдена"));
    }
}
