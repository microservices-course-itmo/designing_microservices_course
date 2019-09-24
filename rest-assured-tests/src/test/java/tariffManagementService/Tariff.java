package tariffManagementService;

import commonTestClasses.FailedResponse;
import io.restassured.response.Response;

import java.util.logging.Logger;

import static commonTestClasses.FailedResponse.jsonToFailedResponse;
import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertEquals;
import static tariffManagementService.TariffObject.*;

public class Tariff {
    private static Logger logger = Logger.getLogger(Logger.class.getName());

    static TariffObject[] getTariffsList() {
        Response response = get("/tariffs");
        assertEquals(200, response.getStatusCode());
        return jsonToTariffsList(response);
    }

    static TariffObject getTariffById(int id) {
        Response response = getTariffById(id, 200);
        return jsonToTariff(response);
    }

    static FailedResponse getTariffByIdWithError(int id) {
        Response response = getTariffById(id, 500);
        return jsonToFailedResponse(response);
    }

    static Response getTariffById(int id, int statusCode) {
        Response response = get("/tariffs/" + id);
        assertEquals(statusCode, response.getStatusCode());
        logger.info("Response status code corresponds to the expected: " + statusCode);
        return response;
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
        logger.info("Response status code corresponds to the expected: " + statusCode);
        return response;
    }

    static TariffObject createTariffByPost(String name, float price, int washingTime) {
        Response response = createTariffByPost(name, price, washingTime, 201);
        logger.info("New tariff has been successfully created");
        return jsonToTariff(response);
    }

    static FailedResponse createTariffByPostWithError(String name, float price, int washingTime) {
        Response response = createTariffByPost(name, price, washingTime, 500);
        logger.info("New tariff hasn't been created");
        return jsonToFailedResponse(response);
    }

    static void deleteTariffById(int id) {
        Response response = delete("/tariffs/" + id);
        assertEquals(200, response.getStatusCode());
        logger.info("TariffManagementService.Tariff with id = " + id + " has been successfully deleted");
    }
}
