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

public class updateTemplateVersion {

    @Test
    public void testUpdateTemplateVersion() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("updateTemplateVersion");
        String path = endpoint.getString("path");
        JSONObject params = endpoint.getJSONObject("params");
        JSONObject body = endpoint.getJSONObject("body");

        String finalPath = path
                .replace("{projectId}", params.getString("projectId"))
                .replace("{templateId}", params.getString("templateId"))
                .replace("{versionId}", params.getString("versionId"));

        // Convert string paths to File objects
        File fileUpload = new File(body.getString("file_upload"));
        File recDoc = new File(body.getString("rec_doc"));

        // Validate files exist
        assert fileUpload.exists() : "❌ file_upload path is invalid!";
        assert recDoc.exists() : "❌ rec_doc path is invalid!";

        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .multiPart("mac_res", body.getString("mac_res"))
                .multiPart("win_res", body.getString("win_res"))
                .multiPart("clients", body.getJSONArray("clients").toString())
                .multiPart("file_upload", fileUpload) // ✅ send as file
                .multiPart("rec_doc", recDoc)         // ✅ send as file
                .when()
                .put();

        response.prettyPrint();
        assertEquals(response.statusCode(), 200, "❌ Expected 200 OK from update");
    }
}