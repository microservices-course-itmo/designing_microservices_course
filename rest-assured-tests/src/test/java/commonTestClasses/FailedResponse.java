package commonTestClasses;

import com.google.gson.Gson;
import io.restassured.response.Response;
import lombok.Getter;

public class FailedResponse {
    private @Getter int status;
    private @Getter String error;
    private @Getter String message;

    public static FailedResponse jsonToFailedResponse(Response response) {
        return new Gson().fromJson(response.asString(), FailedResponse.class);
    }
}
