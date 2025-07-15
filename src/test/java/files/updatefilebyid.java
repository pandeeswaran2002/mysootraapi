package files;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import io.qameta.allure.Allure;

public class updatefilebyid {

    @Test
    public void testUpdateFileMetadata() {
        // 🔹 Load data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("updateFileMetadata");
        String method = endpoint.getString("method");
        String pathTemplate = endpoint.getString("path");

        String workspaceId = endpoint.getJSONObject("params").getString("workspaceId");
        String fileId = endpoint.getJSONObject("params").getString("fileId");
        String finalPath = pathTemplate
                .replace("{workspaceId}", workspaceId)
                .replace("{fileId}", fileId);

        JSONObject body = endpoint.getJSONObject("body");

        // 🔹 Create actual request body JSON
        JSONObject requestBody = new JSONObject();
        requestBody.put("absoluteElements", body.getString("absoluteElements")); // 👈 leave as raw string
        requestBody.put("brandGuidelines", new JSONObject(body.getString("brandGuidelines")));
        requestBody.put("templateFlexibility", new JSONObject(body.getString("templateFlexibility")));


        // 🔹 Send PUT request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)

                .body(requestBody.toString())
                .when()
                .request(method);

        // 🔹 Print response and assert
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Failed to update file metadata.");
    }
}
