package notification;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import io.qameta.allure.Allure;

public class getNotifications {

    @Test
    public void testGetNotifications() {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("getNotifications");
        String path = endpoint.getString("path");

        JSONObject query = endpoint.getJSONObject("query");
        int page = query.getInt("page");
        int pageSize = query.getInt("pageSize");

        // Send GET request
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)
                .queryParam("page", page)
                .queryParam("pageSize", pageSize)
                .when()
                .get();

        // Print and validate response
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Failed to fetch notifications. Expected HTTP 200.");
    }
}
