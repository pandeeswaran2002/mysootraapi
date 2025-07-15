package marketplace;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import io.qameta.allure.Allure;

public class GetMarketPlaceTemplatesTest {

    @Test
    public void testGetMarketPlaceTemplates() {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));
        JSONObject endpoint = json.getJSONObject("getMarketPlaceTemplates");

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");
        String path = endpoint.getString("path");
        JSONObject query = endpoint.getJSONObject("query");

        // Send request
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)
                .queryParam("page", query.getInt("page"))
                .queryParam("pageSize", query.getInt("pageSize"))
                .queryParam("sortBy", query.getString("sortBy"))
                .queryParam("sortOrder", query.getString("sortOrder"))
                .queryParam("search", query.getString("search"))
                .queryParam("type", query.getString("type"))
                .queryParam("status", query.getString("status"))
                .when()
                .get();

        // Validate response
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Failed to fetch templates.");
    }
}
