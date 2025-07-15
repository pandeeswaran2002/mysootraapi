package comments;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class updatecomment {

    @Test
    public void testUpdateComment() {
        // Load JSON config
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("updateComment");
        JSONObject params = endpoint.getJSONObject("params");
        JSONObject body = endpoint.getJSONObject("body");

        // Build dynamic path
        String path = endpoint.getString("path")
                .replace("{projectId}", params.getString("projectId"))
                .replace("{templateId}", params.getString("templateId"))
                .replace("{versionId}", params.getString("versionId"))
                .replace("{commentId}", params.getString("commentId"));

        // Send PUT request
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .put();

        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Failed to update comment.");
    }
}
