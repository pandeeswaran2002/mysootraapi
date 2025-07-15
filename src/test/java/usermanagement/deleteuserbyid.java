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

public class deleteuserbyid {

    @Test
    public void testDeleteUserById() {
        // Load JSON
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonData = new JSONObject(new JSONTokener(is));

        // Global + Endpoint values
        String baseUri = jsonData.getString("baseUri");
        String token = jsonData.getString("token");
        JSONObject deleteUserData = jsonData.getJSONObject("deleteUserById");

        String userId = deleteUserData.getString("userId");
        String basePath = deleteUserData.getString("basePath") + "/" + userId;
        int expectedStatus = deleteUserData.getInt("expectedStatusCode");

        // API call
        Response response = given()
                .baseUri(baseUri)
                .basePath(basePath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .delete();

        response.prettyPrint();
        assertEquals(response.getStatusCode(), expectedStatus, "❌ Failed to delete user.");
    }
}
