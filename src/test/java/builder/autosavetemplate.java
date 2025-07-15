package builder;


import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;


import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class autosavetemplate {

    @Test
    public void testAutosaveTemplate() {
        // Load data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("autosaveTemplate");
        String pathTemplate = endpoint.getString("path");

        String projectId = endpoint.getJSONObject("params").getString("projectId");
        String templateId = endpoint.getJSONObject("params").getString("templateId");
        JSONObject body = endpoint.getJSONObject("body");

        // Replace path params
        String finalPath = pathTemplate
                .replace("{projectId}", projectId)
                .replace("{templateId}", templateId);

        // Make PATCH request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .patch();

        // Print response
        response.prettyPrint();

        // Assertion
        assertEquals(response.statusCode(), 200, "❌ Autosave failed");
    }
}
