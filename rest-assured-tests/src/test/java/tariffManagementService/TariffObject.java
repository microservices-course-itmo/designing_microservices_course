package tariffManagementService;

import com.google.gson.Gson;
import io.restassured.response.Response;

public class TariffObject {
    private int id;
    private String name;
    private float price;
    private int washingTime;

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public int getWashingTime() {
        return washingTime;
    }

    public int getId() {
        return id;
    }

    static TariffObject jsonToTariff(Response response) {
        return new Gson().fromJson(response.asString(), TariffObject.class);
    }

    static TariffObject[] jsonToTariffsList(Response response) {
        return new Gson().fromJson(response.asString(), TariffObject[].class);
    }
}
