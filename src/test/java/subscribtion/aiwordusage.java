package subscribtion;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class aiwordusage {

    @Test
    public void testGetAIWordUsage() {
        // Load configuration
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject data = new JSONObject(new JSONTokener(is));
        String baseUri = data.getString("baseUri");
        String token = data.getString("token"); // Use valid token

        JSONObject endpoint = data.getJSONObject("aiWordUsage");
        RestAssured.baseURI = baseUri;

        // Send GET request
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParams(endpoint.getJSONObject("queryParams").toMap())
                .get(endpoint.getString("path"));

        // Print and validate
        response.prettyPrint();

        assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 OK");

    }
}
