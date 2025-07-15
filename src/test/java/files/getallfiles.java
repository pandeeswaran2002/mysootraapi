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

public class getallfiles {

    @Test
    public void testGetAllFiles() {
        // 🔹 Load config
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("getAllFiles");
        String method = endpoint.getString("method");
        String pathTemplate = endpoint.getString("path");
        String workspaceId = endpoint.getJSONObject("params").getString("workspaceId");

        String finalPath = pathTemplate.replace("{workspaceId}", workspaceId);

        // 🔹 Send request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .request(method);

        // 🔹 Print and assert
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Failed to fetch files.");
    }
}
