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

public class PatchSystemAdminNotificationTest {

    @Test
    public void testPatchSystemAdminNotification() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String defaultToken = json.getString("token");
        String adminToken = json.getString("adminToken");

        JSONObject endpoint = json.getJSONObject("patchSystemAdminNotification");
        String pathTemplate = endpoint.getString("path");
        JSONObject params = endpoint.getJSONObject("params");
        JSONObject body = endpoint.getJSONObject("body");

        // Replace path variables
        String path = pathTemplate.replace("{notificationId}", params.getString("notificationId"));

        // Use admin token for system admin APIs
        String tokenToUse = path.startsWith("/api/system-admin") ? adminToken : defaultToken;

        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + tokenToUse)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .patch();

        response.prettyPrint();
        assertEquals(response.statusCode(), 200, "Expected 200 OK for updating notification read status.");
    }
}
