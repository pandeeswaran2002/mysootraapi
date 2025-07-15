package notification;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import io.qameta.allure.Allure;

public class markAllNotificationsAsRead {

    @Test
    public void testMarkAllAsRead() {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("markAllNotificationsAsRead");
        String path = endpoint.getString("path");

        // Send POST request
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .post();

        // Print and validate response
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Expected HTTP 200 but got " + response.getStatusCode());
    }
}
