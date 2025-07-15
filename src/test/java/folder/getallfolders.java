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

public class getallfolders {

    @Test
    public void testGetAllFolders() {
        // Load JSON configuration
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("getAllFolders");
        String path = endpoint.getString("path");
        JSONObject params = endpoint.getJSONObject("params");
        JSONObject query = endpoint.getJSONObject("query");

        // Replace path variable
        String finalPath = path.replace("{workspaceId}", params.getString("workspaceId"));

        // Send GET request with query params
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .queryParam("parentId", query.getString("parentId"))
                .queryParam("search", query.getString("search"))
                .queryParam("sortBy", query.getString("sortBy"))
                .queryParam("sortOrder", query.getString("sortOrder"))
                .queryParam("page", query.getInt("page"))
                .queryParam("pageSize", query.getInt("pageSize"))
                .when()
                .get();

        // Output and assertions
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Failed to fetch folders.");
    }
}
