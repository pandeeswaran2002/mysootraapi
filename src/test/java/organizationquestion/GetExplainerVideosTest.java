package organizationquestion;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import io.qameta.allure.Allure;

public class GetExplainerVideosTest {

    @Test
    public void testFetchExplainerVideos() {
        // Load data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));
        JSONObject endpoint = json.getJSONObject("getExplainerVideos");

        String baseUri = json.getString("baseUri");
        String path = endpoint.getString("path");
        String token = json.getString("token");

        JSONObject queryParams = endpoint.getJSONObject("queryParams");

        // Send GET request with query params
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)

                .queryParam("search", queryParams.getString("search"))
                .when()
                .get();

        // Output response
        response.prettyPrint();

        // Assertions
        assertEquals(response.getStatusCode(), 200, "❌ Expected status 200 OK");
        assertTrue(response.jsonPath().getList("data").size() > 0, "❌ No videos returned");
    }
}
