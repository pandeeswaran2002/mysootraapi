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

public class deleteVersion {

    @Test
    public void testDeleteVersion() {
        // Load test data from data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("deleteVersion");
        String path = endpoint.getString("path");
        JSONObject params = endpoint.getJSONObject("params");

        String projectId = params.getString("projectId");
        String templateId = params.getString("templateId");
        String versionId = params.getString("versionId");

        // Construct full path
        String finalPath = path
                .replace("{projectId}", projectId)
                .replace("{templateId}", templateId)
                .replace("{versionId}", versionId);

        // Send DELETE request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .delete();

        // Output response
        response.prettyPrint();

        // Assert status code
        assertEquals(response.statusCode(), 200, "❌ Expected HTTP 200 OK for successful deletion");
    }
}
