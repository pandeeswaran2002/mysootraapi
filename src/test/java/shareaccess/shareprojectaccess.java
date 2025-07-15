package shareaccess;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class shareprojectaccess {

    @Test
    public void testShareProjectFolderAccess() {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("shareProjectFolderAccess");
        String path = endpoint.getString("path");
        JSONObject params = endpoint.getJSONObject("params");
        JSONObject body = endpoint.getJSONObject("body");

        // Replace path variable
        String finalPath = path.replace("{projectId}", params.getString("projectId"));

        // Send PUT request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .queryParam("folderId", params.getString("folderId"))
                .contentType("application/json")
                .body(body.toString())
                .when()
                .put();

        // Print and assert
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Failed to share folder access.");
    }
}
