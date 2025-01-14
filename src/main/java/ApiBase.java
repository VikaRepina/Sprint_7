import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiBase {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    static {
        RestAssured.baseURI = BASE_URL;
    }

    public static RequestSpecification getRequestSpecification() {
        return RestAssured.given()
                .contentType(ContentType.JSON);
    }
}
