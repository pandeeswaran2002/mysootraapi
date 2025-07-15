package usermanagement;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class getuserbyid {

    @Test
    public void testGetUserById() {
        // Load JSON config
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonData = new JSONObject(new JSONTokener(is));

        // Extract common and endpoint-specific config
        String baseUri = jsonData.getString("baseUri");
        String token = jsonData.getString("token");
        JSONObject endpointData = jsonData.getJSONObject("getUserById");

        String userId = endpointData.getString("userId"); // Dynamic userId
        String basePath = endpointData.getString("basePath") + "/" + userId;
        int expectedStatus = endpointData.getInt("expectedStatusCode");

        // API Call
        Response response = given()
                .baseUri(baseUri)
                .basePath(basePath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .get();

        response.prettyPrint();
        assertEquals(response.getStatusCode(), expectedStatus, "❌ Failed to fetch user by ID.");
    }
}
