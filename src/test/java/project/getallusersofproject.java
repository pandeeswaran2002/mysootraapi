package project;
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

public class getallusersofproject {

    @Test
    public void testGetProjectUsers() {
        // Load test data from data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject data = new JSONObject(new JSONTokener(is));
        String baseUri = data.getString("baseUri");
        String token = data.getString("token");
        JSONObject userData = data.getJSONObject("getProjectUsers");

        RestAssured.baseURI = baseUri;

        // Send GET request to fetch users
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(userData.getString("path"));

        // Print for debugging
        response.prettyPrint();

        // Validate status code
        assertEquals(response.getStatusCode(), 200, "Expected 200 OK");

    }
}
