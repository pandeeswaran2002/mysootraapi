package notification;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import io.qameta.allure.Allure;

public class updateNotification {

    @Test
    public void testUpdateNotification() {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("updateNotification");
        String path = endpoint.getString("path");
        JSONObject body = endpoint.getJSONObject("body");

        // Send PUT request
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(body.toString())
                .when()
                .put();

        // Print and validate response
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Failed to update notification. Expected HTTP 200.");
    }
}
