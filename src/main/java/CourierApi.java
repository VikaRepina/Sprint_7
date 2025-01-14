import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CourierApi {
    private final Gson gson = new Gson();


    public Response createCourier(Courier courier) {
        String requestBody = gson.toJson(courier);
        return  ApiBase.getRequestSpecification()
                .body(requestBody)
                .post("/api/v1/courier");

    }

    public Response deleteCourier(int courierId, CourierLoginRequest courierLoginRequest) {
        String requestBodyId = gson.toJson(courierLoginRequest);
        Response responseId = ApiBase.getRequestSpecification()
                .body(requestBodyId)
                .post("/api/v1/courier/login");
        courierId = responseId.jsonPath().get("id");
        if (courierId > 0) {
            return ApiBase.getRequestSpecification()
                    .delete("/api/v1/courier/" + courierId);
        }
        return null;
    }

    public Response authorizationCourier(CourierLoginRequest courierLoginRequest) {
        String requestBodyId = gson.toJson(courierLoginRequest);
        return ApiBase.getRequestSpecification()
                .body(requestBodyId)
                .post("/api/v1/courier/login");
    }

}
