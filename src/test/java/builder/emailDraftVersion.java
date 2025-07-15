package builder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class emailDraftVersion {

    @Test
    public void testEmailDraftVersion() {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("emailDraftVersion");
        String pathTemplate = endpoint.getString("path");

        String projectId = endpoint.getJSONObject("params").getString("projectId");
        String templateId = endpoint.getJSONObject("params").getString("templateId");
        String versionId = endpoint.getJSONObject("params").getString("versionId");

        JSONObject body = endpoint.getJSONObject("body");

        // Construct final path
        String finalPath = pathTemplate
                .replace("{projectId}", projectId)
                .replace("{templateId}", templateId)
                .replace("{versionId}", versionId);

        // Send POST request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .post();

        // Print and validate response
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Email draft failed.");
    }
}
