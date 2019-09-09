import com.google.gson.Gson;
import io.restassured.response.Response;

public class Tariff {
    public int id;
    public String name;
    public float price;
    public int washingTime;

    public static Tariff jsonToTariff(Response response) {
        return new Gson().fromJson(response.asString(), Tariff.class);
    }

    public static Tariff[] jsonToTariffsList(Response response) {
        return new Gson().fromJson(response.asString(), Tariff[].class);
    }
}
