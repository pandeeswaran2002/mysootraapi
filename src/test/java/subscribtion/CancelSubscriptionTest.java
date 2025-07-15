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
import static org.testng.Assert.assertTrue;

public class CancelSubscriptionTest {

    @Test
    public void testCancelSubscription() {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject data = new JSONObject(new JSONTokener(is));
        String baseUri = data.getString("baseUri");
        String token = data.getString("token"); // Replace with actual token logic
        JSONObject endpoint = data.getJSONObject("cancelSubscription");

        RestAssured.baseURI = baseUri;

        // Make POST request
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .post(endpoint.getString("path"));

        // Debug response
        response.prettyPrint();

        // Assert status
        assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 OK");


    }
}
