import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CourierApi {
    private static final String URL = "https://qa-scooter.praktikum-services.ru";
    private final Gson gson = new Gson();

    public Response createCourier(Courier courier) {
        String requestBody = gson.toJson(courier);
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/v1/courier");

    }

    public Response deleteCourier(int courierId, CourierLoginRequest courierLoginRequest) {
        String requestBodyId = gson.toJson(courierLoginRequest);
        Response responseId = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBodyId)
                .post(URL + "/api/v1/courier/login");
        courierId = responseId.jsonPath().get("id");
        if (courierId > 0) {
            return RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .delete(URL + "/api/v1/courier/" + courierId);
        }
        return null;
    }

    public Response AuthorizationCourier(CourierLoginRequest courierLoginRequest) {
        String requestBodyId = gson.toJson(courierLoginRequest);
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBodyId)
                .post(URL + "/api/v1/courier/login");
    }

}
