import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import io.qameta.allure.Allure;

public class GetMarketoTokenTest {

    @Test
    public void testGetMarketoToken() {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));
        JSONObject endpoint = json.getJSONObject("getMarketoToken");

        String baseUri = json.getString("baseUri");
        String path = endpoint.getString("path");
        String token = json.getString("token");

        // Send request
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)
                .when()
                .get();

        // Debug output
        response.prettyPrint();

        // Basic validations
        assertEquals(response.getStatusCode(), 200, "❌ Expected HTTP 200 OK");
        assertTrue(response.jsonPath().getString("access_token") != null, "❌ Missing access token in response");
    }
}
