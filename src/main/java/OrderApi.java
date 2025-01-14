import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class OrderApi {
    private static final String URL = "https://qa-scooter.praktikum-services.ru";
    private final Gson gson = new Gson();

    public Response CreateOrder(Order order) {
        String requestBody = gson.toJson(order);
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(URL + "/api/v1/orders");
    }
    public Response deleteOrder(int orderTrack) {
        if (orderTrack > 0) {
            return RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .put(URL + "/api/v1/orders/cancel?track=" + orderTrack);
        }
        return null;
    }

    public Response ReceiveOrderList() {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .get("/api/v1/orders?limit=10&page=0");
    }
}
