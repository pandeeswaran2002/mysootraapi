import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import io.qameta.allure.Allure;

public class FetchMarketoFullContent {

    @Test
    public void testGetFullTemplateContent() {
        // Load test data from data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");
        JSONObject endpoint = json.getJSONObject("getMarketoFullContent");

        String path = endpoint.getString("path");
        String type = endpoint.getString("type");
        String templateId = endpoint.getString("templateId");

        // Send GET request with headers and query parameters
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)
                .queryParam("type", type)
                .queryParam("templateId", templateId)
                .when()
                .get();

        // Print response for debugging
        response.prettyPrint();

        // Basic assertions
        assertEquals(response.statusCode(), 200, "❌ Expected HTTP 200 OK");
        String content = response.jsonPath().getString("content");
        assertNotNull(content, "❌ Content should not be null");
    }
}
