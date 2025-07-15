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

public class customerdetails {

    @Test
    public void testGetCustomerDetails() {
        // Load config
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null : "data.json not found on classpath";
        JSONObject config = new JSONObject(new JSONTokener(is));

        String baseUri = config.getString("baseUri");
        String token   = config.getString("token");
        JSONObject endpoint = config.getJSONObject("getCustomerDetails");

        RestAssured.baseURI = baseUri;

        // Send GET request
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(endpoint.getString("path"));

        // Debug output
        response.prettyPrint();

        // Assertions
        assertEquals(response.getStatusCode(), 200, "❌ Expected HTTP 200 OK");

    }
}
