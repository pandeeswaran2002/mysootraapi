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

public class deletefilebyid {

    @Test
    public void testDeleteFile() {
        // 🔹 Load config
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("deleteFile");
        String method = endpoint.getString("method");
        String pathTemplate = endpoint.getString("path");

        String workspaceId = endpoint.getJSONObject("params").getString("workspaceId");
        String fileId = endpoint.getJSONObject("params").getString("fileId");

        String finalPath = pathTemplate
                .replace("{workspaceId}", workspaceId)
                .replace("{fileId}", fileId);

        // 🔹 Send DELETE request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .request(method);

        // 🔹 Output and assert
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ File deletion failed.");
    }
}
