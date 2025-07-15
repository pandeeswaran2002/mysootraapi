package usermanagement;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;
import io.qameta.allure.Allure;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class getalluser {

    @Test
    public void testGetAllUsers() {
        // Load shared config
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonData = new JSONObject(new JSONTokener(is));

        // Extract common + endpoint-specific config
        String baseUri = jsonData.getString("baseUri");
        String token = jsonData.getString("token");
        JSONObject userData = jsonData.getJSONObject("getAllUsers");

        String basePath = userData.getString("basePath");
        int expectedStatus = userData.getInt("expectedStatusCode");

        // Perform GET request
        Response response = given()
                .baseUri(baseUri)
                .basePath(basePath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .get();

        response.prettyPrint();
        assertEquals(response.getStatusCode(), expectedStatus, "❌ Failed to fetch users.");
    }
}
