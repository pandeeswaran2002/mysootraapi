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

public class getuseractivity {

    @Test
    public void testGetUserActivity() {
        // Load JSON config
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonData = new JSONObject(new JSONTokener(is));

        String baseUri = jsonData.getString("baseUri");
        String token = jsonData.getString("token");

        JSONObject activityData = jsonData.getJSONObject("getUserActivity");

        int page = activityData.getInt("page");
        int pageSize = activityData.getInt("pageSize");
        String basePath = activityData.getString("basePath");
        int expectedStatus = activityData.getInt("expectedStatusCode");

        Response response = given()
                .baseUri(baseUri)
                .basePath(basePath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .queryParam("page", page)
                .queryParam("pageSize", pageSize)
                .when()
                .get();

        response.prettyPrint();
        assertEquals(response.statusCode(), expectedStatus, "❌ Status Code mismatch.");
    }
}
