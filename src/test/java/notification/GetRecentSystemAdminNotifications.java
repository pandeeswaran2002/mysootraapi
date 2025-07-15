package notification;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import io.qameta.allure.Allure;

public class GetRecentSystemAdminNotifications {

    @Test
    public void testGetRecentSystemAdminNotifications() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String defaultToken = json.getString("token");
        String adminToken = json.getString("adminToken");

        JSONObject endpoint = json.getJSONObject("getRecentSystemAdminNotifications");
        String path = endpoint.getString("path");

        // Admin token required for system-admin endpoints
        String tokenToUse = path.startsWith("/api/system-admin") ? adminToken : defaultToken;

        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + tokenToUse)
                .when()
                .get();

        response.prettyPrint();
        assertEquals(response.statusCode(), 200, "Expected 200 OK for recent system admin notifications.");
    }
}
