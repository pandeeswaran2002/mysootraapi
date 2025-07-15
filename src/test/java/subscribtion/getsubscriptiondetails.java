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
import static org.testng.Assert.assertTrue;

public class getsubscriptiondetails {

    @Test
    public void testGetSubscriptionDetails() {
        // Load data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject data = new JSONObject(new JSONTokener(is));
        String baseUri = data.getString("baseUri");
        String token = data.getString("token"); // Assume Bearer token auth
        JSONObject endpoint = data.getJSONObject("getSubscription");

        RestAssured.baseURI = baseUri;

        // Send GET request
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(endpoint.getString("path"));

        // Debug output
        response.prettyPrint();

        // Assertions
        assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 OK");


    }
}
