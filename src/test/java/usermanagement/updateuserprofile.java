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

public class updateuserprofile {

    @Test
    public void testUpdateUserProfile() {
        // Load JSON file
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonData = new JSONObject(new JSONTokener(is));

        // Get API-specific section
        JSONObject updateProfileData = jsonData.getJSONObject("updateUserProfile");

        // Common values
        String baseUri = jsonData.getString("baseUri");
        String token = jsonData.getString("token");
        String basePath = updateProfileData.getString("basePath");
        int expectedStatus = updateProfileData.getInt("expectedStatusCode");
        JSONObject body = updateProfileData.getJSONObject("body");

        // API call
        Response response = given()
                .baseUri(baseUri)
                .basePath(basePath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .put();

        response.prettyPrint();
        assertEquals(response.getStatusCode(), expectedStatus, "❌ Unexpected status code");
    }
}
