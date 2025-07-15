package versions;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class updateVersionTitle {

    @Test
    public void testUpdateVersionTitle() {
        // Load JSON test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("updateVersionTitle");
        String path = endpoint.getString("path");

        JSONObject params = endpoint.getJSONObject("params");
        String projectId = params.getString("projectId");
        String templateId = params.getString("templateId");
        String versionId = params.getString("versionId");

        String finalPath = path
                .replace("{projectId}", projectId)
                .replace("{templateId}", templateId)
                .replace("{versionId}", versionId);

        JSONObject body = endpoint.getJSONObject("body");

        // Send PUT request to update the title
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .put();

        // Print and validate
        response.prettyPrint();
        assertEquals(response.statusCode(), 200, "❌ Title update failed. Expected HTTP 200.");
    }
}
