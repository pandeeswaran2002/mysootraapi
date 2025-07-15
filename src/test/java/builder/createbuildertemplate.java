package builder;



import io.qameta.allure.Allure;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.File;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class createbuildertemplate {

    @Test
    public void testCreateBuilderTemplate() {
        // 🔹 Load config
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("createBuilderTemplate");
        String method = endpoint.getString("method");
        String pathTemplate = endpoint.getString("path");
        String projectId = endpoint.getJSONObject("params").getString("projectId");

        JSONObject formData = endpoint.getJSONObject("formData");
        String finalPath = pathTemplate.replace("{projectId}", projectId);
        File file = new File(formData.getString("fileUpload"));

        // 🔹 Send multipart/form-data request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .multiPart("fileUpload", file)
                .multiPart("name", formData.getString("name"))
                .multiPart("description", formData.getString("description"))
                .multiPart("type", formData.getString("type"))
                .multiPart("source", formData.getString("source"))
                .multiPart("prompt", formData.getString("prompt"))
                .multiPart("deadLine", formData.getString("deadLine"))
                .multiPart("status", formData.getString("status"))
                .multiPart("content", formData.getString("content"))
                .multiPart("folderId", formData.getString("folderId"))
                .multiPart("importTemplateId", formData.getString("importTemplateId"))
                .multiPart("questions", formData.getString("questions"))
                .when()
                .request(method);

        // 🔹 Print response and assert
        response.prettyPrint();
        assertEquals(response.statusCode(), 201, "❌ Failed to create builder template.");
    }
}
