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

public class CourierTest {
    private static final String Login = "VikaWR";
    private static final String Password = "56123";
    private static final String FirstName = "Test";
    private int courierId;
    private final Gson gson = new Gson();


    @After
    @Step("Находит id созданного курьера и удаляет его из БД")
    public void tearDown() {
        CourierApi courierApi = new CourierApi();
        CourierLoginRequest loginRequest = new CourierLoginRequest(Login, Password);
        Response response = courierApi.deleteCourier(courierId, loginRequest);

        if (response != null) {
            response.then().statusCode(200);
        }
    }

    @Test
    @DisplayName("Test create courier")
    @Description("Проверка на успешное создание курьера")
    public void testCreateCourier() {
        CourierApi courierApi = new CourierApi();
        Courier courier = new Courier(Login, Password, FirstName);
        Response response = courierApi.createCourier(courier);
        response.then().statusCode(201);
        response.then().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Test duplicate create courier")
    @Description("Проверка на вывод ошибки, при создании одинаковых курьеров")
    public void testDuplicateCreateCourier() {
        CourierApi courierApi = new CourierApi();
        Courier courier = new Courier(Login, Password, FirstName);
        Response createResponse = courierApi.createCourier(courier);
        if (createResponse.statusCode() == 201) {
            Response response = courierApi.createCourier(courier);
            response.then().statusCode(409);
            response.then().body("message", equalTo("Этот логин уже используется."));
        }
    }

    @Test
    @DisplayName("Test create courier with existing login")
    @Description("Проверка на вывод ошибки, если создать курьера с уже существующим логином")
    public void testCreateCourierWithExistingLogin() {
        CourierApi courierApi = new CourierApi();
        Courier courier = new Courier(Login, Password, FirstName);
        Response response = courierApi.createCourier(courier);
        response.then().statusCode(201);

        Courier existingCourier = new Courier(Login, "54789", "Testing");
        Response responseSecond = courierApi.createCourier(existingCourier);
        responseSecond.then().statusCode(409);
        responseSecond.then().body("message", equalTo("Этот логин уже используется."));
    }
}
