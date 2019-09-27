package commonTestClasses;

import com.google.gson.Gson;
import io.restassured.response.Response;

public class FailedResponse {
    private int status;
    private String error;
    private String message;

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public static FailedResponse jsonToFailedResponse(Response response) {
        return new Gson().fromJson(response.asString(), FailedResponse.class);
    }
}
