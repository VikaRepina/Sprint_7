import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class OrderApi {
    private final Gson gson = new Gson();

    public Response createOrder(Order order) {
        String requestBody = gson.toJson(order);
        return ApiBase.getRequestSpecification()
                .body(requestBody)
                .post("/api/v1/orders");
    }
    public Response deleteOrder(int orderTrack) {
        if (orderTrack > 0) {
            return ApiBase.getRequestSpecification()
                    .put("/api/v1/orders/cancel?track=" + orderTrack);
        }
        return null;
    }

    public Response receiveOrderList() {
        return ApiBase.getRequestSpecification()
                .get("/api/v1/orders?limit=10&page=0");
    }
}
