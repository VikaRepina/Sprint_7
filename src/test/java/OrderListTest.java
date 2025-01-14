import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class OrderListTest {
    private static final String URL = "https://qa-scooter.praktikum-services.ru";
    private final Gson gson = new Gson();

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
    }

    @Test
    @DisplayName("Test receive order list")
    @Description("Проверка что в тело ответа возвращается список заказов")
    public void testReceiveOrderList() {
        OrderApi orderApi = new OrderApi();
        Response response = orderApi.ReceiveOrderList();
        response.then().statusCode(200);
        response.then().body("orders", notNullValue());
        response.then().body("orders.id", notNullValue());
        response.then().body("orders.id", everyItem(isA(Number.class)));
    }

}
