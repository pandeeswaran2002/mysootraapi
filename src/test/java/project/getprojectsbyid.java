package project;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;
import io.qameta.allure.Allure;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class getprojectsbyid {

    @Test
    public void testGetProjectById() {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject data = new JSONObject(new JSONTokener(is));

        String baseUri = data.getString("baseUri");
        String token = data.getString("token");
        JSONObject endpoint = data.getJSONObject("getProjectById");

        RestAssured.baseURI = baseUri;

        // Send GET request
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(endpoint.getString("path"));

        // Output response
        response.prettyPrint();

        // Validate response
        assertEquals(response.statusCode(), 200, "❌ Expected HTTP 200 OK");

    }
}
