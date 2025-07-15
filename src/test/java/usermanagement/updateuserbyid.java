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

public class updateuserbyid {

    @Test
    public void testUpdateUserById() {
        // Load data from JSON
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonData = new JSONObject(new JSONTokener(is));

        // Extract global + endpoint values
        String baseUri = jsonData.getString("baseUri");
        String token = jsonData.getString("token");
        JSONObject updateUserData = jsonData.getJSONObject("updateUserById");

        String userId = updateUserData.getString("userId");
        String basePath = updateUserData.getString("basePath") + "/" + userId;
        int expectedStatus = updateUserData.getInt("expectedStatusCode");
        JSONObject body = updateUserData.getJSONObject("body");

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
        assertEquals(response.getStatusCode(), expectedStatus, "❌ Failed to update user details.");
    }
}
