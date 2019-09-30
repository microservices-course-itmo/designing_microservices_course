package commonTestClasses;

import com.google.gson.Gson;
import io.restassured.response.Response;
import lombok.Getter;

@Getter
public class FailedResponse {
    private int status;
    private String error;
    private String message;

    public static FailedResponse jsonToFailedResponse(Response response) {
        return new Gson().fromJson(response.asString(), FailedResponse.class);
    }
}
