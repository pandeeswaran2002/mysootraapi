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

public class getplanusage {

    @Test
    public void testGetPlanUsage() {
        // Load config from JSON
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject data = new JSONObject(new JSONTokener(is));
        String baseUri = data.getString("baseUri");
        String token = data.getString("token"); // Replace or load token dynamically
        JSONObject endpoint = data.getJSONObject("getPlanUsage");

        RestAssured.baseURI = baseUri;

        // Make GET request
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .get(endpoint.getString("path"));

        // Output response
        response.prettyPrint();

        // Basic assertions
        assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 OK");


    }
}
