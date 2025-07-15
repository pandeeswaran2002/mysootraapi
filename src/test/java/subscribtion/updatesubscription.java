package subscribtion;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class updatesubscription {

    @Test
    public void testUpdateSubscription() {
        // Load config from JSON
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject data = new JSONObject(new JSONTokener(is));
        String baseUri = data.getString("baseUri");
        String token = data.getString("token"); // Replace with your valid Bearer token
        JSONObject endpoint = data.getJSONObject("updateSubscription");

        RestAssured.baseURI = baseUri;

        // Prepare the request body
        JSONObject requestBody = endpoint.getJSONObject("body");

        // Send POST request
        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody.toString())
                .post(endpoint.getString("path"));

        // Print response
        response.prettyPrint();

        // Basic assertions
        assertEquals(response.statusCode(), 200, "Expected HTTP 200 OK");

    }
}
