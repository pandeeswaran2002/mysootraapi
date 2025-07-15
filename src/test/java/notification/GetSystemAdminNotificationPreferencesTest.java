package notification;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import io.qameta.allure.Allure;


public class GetSystemAdminNotificationPreferencesTest {

    @Test
    public void testGetSystemAdminNotificationPreferences() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String defaultToken = json.getString("token");
        String adminToken = json.getString("adminToken");

        JSONObject endpoint = json.getJSONObject("getSystemAdminNotificationPreferences");
        String path = endpoint.getString("path");

        String tokenToUse = path.startsWith("/api/system-admin") ? adminToken : defaultToken;

        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + tokenToUse)
                .when()
                .get();

        response.prettyPrint();
        assertEquals(response.statusCode(), 200, "Expected 200 OK when retrieving system admin notification preferences.");
    }
}
