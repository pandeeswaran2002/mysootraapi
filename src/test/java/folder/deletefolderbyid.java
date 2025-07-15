package folder;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import io.qameta.allure.Allure;

public class deletefolderbyid {

    @Test
    public void testDeleteFolder() {
        // Load test data from JSON file
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("deleteFolder");
        String path = endpoint.getString("path");
        JSONObject params = endpoint.getJSONObject("params");

        // Replace path variables
        String finalPath = path
                .replace("{workspaceId}", params.getString("workspaceId"))
                .replace("{folderId}", params.getString("folderId"));

        // Send DELETE request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete();

        // Output and assertions
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Folder deletion failed.");
    }
}
