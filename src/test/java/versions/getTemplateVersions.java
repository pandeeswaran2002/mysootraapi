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
import static org.testng.Assert.assertTrue;

public class getTemplateVersions {

    @Test
    public void testGetTemplateVersions() {
        // Load data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("getTemplateVersions");
        String pathTemplate = endpoint.getString("path");

        JSONObject params = endpoint.getJSONObject("params");
        String projectId = params.getString("projectId");
        String templateId = params.getString("templateId");
        int page = params.getInt("page");
        int pageSize = params.getInt("pageSize");

        // Build final path
        String finalPath = pathTemplate
                .replace("{projectId}", projectId)
                .replace("{templateId}", templateId);

        // Send GET request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .queryParam("page", page)
                .queryParam("pageSize", pageSize)
                .contentType(ContentType.JSON)
                .when()
                .get();

        // Print response
        response.prettyPrint();

        // Assertions
        assertEquals(response.statusCode(), 200, "❌ Failed to fetch template versions");

    }
}
