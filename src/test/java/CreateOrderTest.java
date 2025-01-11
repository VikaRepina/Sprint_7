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

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private static final String URL = "https://qa-scooter.praktikum-services.ru";

    private String firstName;
    private String lastName;
    private String address;
    private int metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    public CreateOrderTest(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Before
    public void setup() {
        RestAssured.baseURI = URL;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String[]{"BLACK"}},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String[]{"GREY"}},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String[]{"BLACK", "GREY"}},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String[]{}},
        });
    }

    @Test
    @DisplayName("Test create order with color selection")
    @Description("Проверка на создание заказа, если совсем не указывать цвет, указать оба цвета или один")
    public void testCreateOrderWithColorSelection() {
        String requestBody = String.format("{\"firstName\": \"%s\", \"lastName\": \"%s\", \"address\": \"%s\", \"metroStation\": %s, \"phone\": \"%s\", \"rentTime\": %s, \"deliveryDate\": \"%s\", \"comment\": \"%s\", \"color\": [%s]}",
                firstName != null ? firstName : "",
                lastName != null ? lastName : "",
                address != null ? address : "",
                metroStation,
                phone != null ? phone : "",
                rentTime,
                deliveryDate != null ? deliveryDate : "",
                comment != null ? comment : "",
                color != null ? String.join(", ", Arrays.stream(color).map(c -> "\"" + c + "\"").toArray(String[]::new)) : "");
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/v1/orders");
        response.then().statusCode(201);
        response.then().body("track", notNullValue());
    }
}
