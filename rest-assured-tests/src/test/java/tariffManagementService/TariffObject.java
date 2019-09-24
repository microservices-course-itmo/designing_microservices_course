package tariffManagementService;

import com.google.gson.Gson;
import io.restassured.response.Response;

class TariffObject {
    int id;
    String name;
    float price;
    int washingTime;

    static TariffObject jsonToTariff(Response response) {
        return new Gson().fromJson(response.asString(), TariffObject.class);
    }

    static TariffObject[] jsonToTariffsList(Response response) {
        return new Gson().fromJson(response.asString(), TariffObject[].class);
    }
}
