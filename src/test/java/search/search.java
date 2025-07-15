package search;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class search {

    @Test
    public void testGlobalSearch() {
        // Load data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject config = new JSONObject(new JSONTokener(is));

        // Get endpoint and credentials
        JSONObject endpoint = config.getJSONObject("globalSearch");
        String baseUri = config.getString("baseUri");
        String path = endpoint.getString("path");
        String token = config.getString("token"); // fixed this line
        JSONObject queryParams = endpoint.getJSONObject("queryParams");

        // Perform GET request
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)
                .queryParam("search", queryParams.getString("search"))
                .when()
                .get();

        // Print response for debugging
        response.prettyPrint();

        // Validate status and response structure
        assertEquals(response.getStatusCode(), 200, "Expected HTTP status 200 OK");

    }
}
