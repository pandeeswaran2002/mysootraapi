package files;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.File;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import io.qameta.allure.Allure;

public class createnewfile {

    @Test
    public void testCreateFile() {
        // 🔹 Load config
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("createFile");
        String method = endpoint.getString("method");
        String pathTemplate = endpoint.getString("path");
        String workspaceId = endpoint.getJSONObject("params").getString("workspaceId");
        String finalPath = pathTemplate.replace("{workspaceId}", workspaceId);

        JSONObject fileObj = endpoint.getJSONObject("file");
        String filePath = fileObj.getString("filePath");
        String fieldName = fileObj.getString("fieldName");

        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("❌ File not found: " + filePath);
        }

        JSONObject body = endpoint.getJSONObject("body");

        // 🔹 Send request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .multiPart(fieldName, file)
                .multiPart("absoluteElements", body.getString("absoluteElements"))
                .multiPart("brandGuidelines", body.getString("brandGuidelines"))
                .multiPart("templateFlexibility", body.getString("templateFlexibility"))
                .when()
                .request(method);

        response.prettyPrint();
        assertEquals(response.getStatusCode(), 201, "❌ File creation failed.");
    }
}
