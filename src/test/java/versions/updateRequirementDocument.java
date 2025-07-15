package versions;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.File;
import java.io.InputStream;

import static org.testng.Assert.assertEquals;

public class updateRequirementDocument {

    @Test
    public void testUpdateRequirementDoc() {
        // Load data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("updateRequirementDocument");
        String path = endpoint.getString("path");
        JSONObject params = endpoint.getJSONObject("params");
        JSONObject fileInfo = endpoint.getJSONObject("file");

        // Replace path variables
        String finalPath = path
                .replace("{projectId}", params.getString("projectId"))
                .replace("{templateId}", params.getString("templateId"))
                .replace("{versionId}", params.getString("versionId"));

        File file = new File(fileInfo.getString("filePath"));

        Response response = RestAssured
                .given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .multiPart(fileInfo.getString("fieldName"), file)
                .when()
                .put();

        response.prettyPrint();

        assertEquals(response.statusCode(), 200, "❌ Requirement document upload failed!");
    }
}
