package tariffManagementService;

import com.google.gson.Gson;
import io.restassured.response.Response;
import lombok.Getter;

public class TariffObject {
    private @Getter int id;
    private @Getter String name;
    private @Getter float price;
    private @Getter int washingTime;

    static TariffObject jsonToTariff(Response response) {
        return new Gson().fromJson(response.asString(), TariffObject.class);
    }

    static TariffObject[] jsonToTariffsList(Response response) {
        return new Gson().fromJson(response.asString(), TariffObject[].class);
    }
}
