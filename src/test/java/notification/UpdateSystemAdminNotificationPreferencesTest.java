package notification;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import io.qameta.allure.Allure;

public class UpdateSystemAdminNotificationPreferencesTest {

    @Test
    public void testUpdateSystemAdminNotificationPreferences() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String defaultToken = json.getString("token");
        String adminToken = json.getString("adminToken");

        JSONObject endpoint = json.getJSONObject("updateSystemAdminNotificationPreferences");
        String path = endpoint.getString("path");
        JSONObject requestBody = endpoint.getJSONObject("body");

        String tokenToUse = path.startsWith("/api/system-admin") ? adminToken : defaultToken;

        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + tokenToUse)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .put();

        response.prettyPrint();
        assertEquals(response.statusCode(), 200, "Expected 200 OK when updating notification preferences.");
    }
}
