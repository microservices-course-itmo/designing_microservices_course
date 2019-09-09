import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertEquals;

public class TariffManagementServiceClass {

    static Tariff[] getTariffsList() {
        Response response = get("/tariffs");
        assertEquals(200, response.getStatusCode());
        return Tariff.jsonToTariffsList(response);
    }

    static Tariff getTariffById(int id) {
        Response response = get("/tariffs/" + id);
        assertEquals(200, response.getStatusCode());
        return Tariff.jsonToTariff(response);
    }

    static Response createTariffByPost(String name, float price, int washingTime, int statusCode) {
        Response response = given()
                .body("{\n" +
                        "  \"name\": \"" + name + "\"," +
                        "  \"price\": " + price + "," +
                        "  \"washingTime\": " + washingTime + "" +
                        "}")
                .contentType("application/json;charset=UTF-8")
                .post("/tariffs");
        assertEquals(statusCode, response.getStatusCode());
        System.out.println("LOG: Response status code corresponds to the expected: " + statusCode);
        return response;
    }

    static Tariff createTariffByPost(String name, float price, int washingTime) {
        Response response = createTariffByPost(name, price, washingTime, 201);
        System.out.println("LOG: New tariff has been successfully created");
        return Tariff.jsonToTariff(response);
    }
}
