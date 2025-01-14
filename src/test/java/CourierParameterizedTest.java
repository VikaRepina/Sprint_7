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
public class CourierParameterizedTest {
    private Integer courierId;
    private final Gson gson = new Gson();

    private String login;
    private String password;
    private String firstName;

    public CourierParameterizedTest(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
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
        CourierApi courierApi = new CourierApi();
        Courier courier = new Courier(login, password, firstName);
        Response response = courierApi.createCourier(courier);
        response.then().statusCode(400);
        response.then().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
