package commonTestClasses;

import com.google.gson.Gson;
import io.restassured.response.Response;

public class FailedResponse {
    public int status;
    public String error;
    public String message;

    public static FailedResponse jsonToFailedResponse(Response response) {
        return new Gson().fromJson(response.asString(), FailedResponse.class);
    }
}
