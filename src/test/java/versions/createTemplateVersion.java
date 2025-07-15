package versions;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.File;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class createTemplateVersion {

    @Test
    public void testCreateTemplateVersion() {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("createTemplateVersion");
        String pathTemplate = endpoint.getString("path");

        JSONObject params = endpoint.getJSONObject("params");
        String projectId = params.getString("projectId");
        String templateId = params.getString("templateId");

        // Replace placeholders
        String finalPath = pathTemplate
                .replace("{projectId}", projectId)
                .replace("{templateId}", templateId);

        JSONObject formData = endpoint.getJSONObject("formData");
        JSONObject files = endpoint.getJSONObject("files");

        File fileUpload = new File(files.getString("fileUpload"));
        File recDoc = new File(files.getString("rec_doc"));

        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .multiPart("name", formData.getString("name"))

                .multiPart("status", formData.getString("status"))
                .multiPart("content", formData.getString("content"))
                .multiPart("importTemplateId", formData.getString("importTemplateId"))
                .multiPart("mac_res", formData.getString("mac_res"))
                .multiPart("win_res", formData.getString("win_res"))
                .multiPart("configs", formData.getJSONObject("configs").toString())
                .multiPart("fileUpload", fileUpload)
                .multiPart("rec_doc", recDoc)
                .multiPart("clients", String.join(",", formData.getJSONArray("clients").toList().stream().map(Object::toString).toList()))
                .contentType(ContentType.MULTIPART)
                .when()
                .post();

        // Response logging
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Failed to create new template version.");
    }
}
