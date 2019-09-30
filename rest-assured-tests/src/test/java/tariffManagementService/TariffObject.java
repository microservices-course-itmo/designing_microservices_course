package tariffManagementService;

import com.google.gson.Gson;
import io.restassured.response.Response;
import lombok.Getter;

@Getter
public class TariffObject {
    private int id;
    private String name;
    private float price;
    private int washingTime;

    static TariffObject jsonToTariff(Response response) {
        return new Gson().fromJson(response.asString(), TariffObject.class);
    }

    static TariffObject[] jsonToTariffsList(Response response) {
        return new Gson().fromJson(response.asString(), TariffObject[].class);
    }
}
