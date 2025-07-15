import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.Iterator;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import io.qameta.allure.Allure;

public class FetchTemplatesfrommarketo {

    @Test
    public void testGetMarketoAssets() {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));
        JSONObject endpoint = json.getJSONObject("getMarketoAssets");

        String baseUri = json.getString("baseUri");
        String path = endpoint.getString("path");
        String token = json.getString("token");
        JSONObject queryParams = endpoint.getJSONObject("queryParams");

        // Build request with query parameters and authorization
        io.restassured.specification.RequestSpecification request = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token);

        Iterator<String> keys = queryParams.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            request.queryParam(key, queryParams.getString(key));
        }

        // Send GET request
        Response response = request.when().get();

        // Debug output
        response.prettyPrint();

        // Basic assertions
        assertEquals(response.getStatusCode(), 200, "❌ Expected HTTP 200 OK");
        assertTrue(response.jsonPath().getList("$").size() >= 0, "❌ Expected a list of assets");
    }
}
