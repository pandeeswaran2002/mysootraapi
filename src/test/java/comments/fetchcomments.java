package comments;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class fetchcomments {

    @Test
    public void testGetComments() {
        // Load JSON config
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("getComments");
        JSONObject params = endpoint.getJSONObject("params");
        JSONObject query = endpoint.getJSONObject("query");

        // Build dynamic path
        String path = endpoint.getString("path")
                .replace("{projectId}", params.getString("projectId"))
                .replace("{templateId}", params.getString("templateId"))
                .replace("{versionId}", params.getString("versionId"));

        // Execute request
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)
                .queryParam("page", query.getInt("page"))
                .queryParam("pageSize", query.getInt("pageSize"))
                .queryParam("type", query.getString("type"))
                .when()
                .get();

        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Failed to fetch comments.");
    }
}
