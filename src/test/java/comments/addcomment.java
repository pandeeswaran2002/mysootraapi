package comments;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;

import java.io.File;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class addcomment {

    @Test
    public void testAddCommentToVersion() {
        // Load JSON
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");
        JSONObject endpoint = json.getJSONObject("addComment");

        // Prepare path
        String path = endpoint.getString("path");
        JSONObject params = endpoint.getJSONObject("params");
        path = path.replace("{projectId}", params.getString("projectId"))
                .replace("{templateId}", params.getString("templateId"))
                .replace("{versionId}", params.getString("versionId"));

        JSONObject form = endpoint.getJSONObject("form");

        // Prepare file (optional)
        File file = null;
        String fieldName = "";
        String contentType = "";
        if (endpoint.has("file")) {
            JSONObject fileData = endpoint.getJSONObject("file");
            file = new File(fileData.getString("filePath"));
            fieldName = fileData.getString("fieldName");
            contentType = fileData.getString("contentType");
            if (!file.exists()) {
                throw new RuntimeException("Attachment file not found.");
            }
        }

        // Build request
        io.restassured.specification.RequestSpecification request = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)
                .multiPart("content", form.getString("content"))
                .multiPart("coordinates", form.getString("coordinates"))
                .multiPart("deviceId", form.getString("deviceId"))
                .multiPart("taggedUsers", form.getString("taggedUsers"))

                .multiPart("type", form.getString("type"));

        // Optional fields
        if (!form.getString("parentCommentId").isEmpty()) {
            request.multiPart("parentCommentId", form.getString("parentCommentId"));
        }

        if (file != null) {
            request.multiPart(fieldName, file, contentType);
        }

        Response response = request.when().post();

        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Failed to post comment.");
    }
}
