package builder;


import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class addComponentToLibrary {

    @Test
    public void testAddComponentToLibrary() {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.optString("baseUri");
        String token = json.optString("token");

        JSONObject endpoint = json.getJSONObject("addComponentToLibrary");
        String pathTemplate = endpoint.optString("path");
        int expectedStatusCode = endpoint.optInt("expectedStatusCode", 201); // Default to 201

        // Extract path params
        String projectId = endpoint.getJSONObject("params").optString("projectId");
        String templateId = endpoint.getJSONObject("params").optString("templateId");
        JSONObject body = endpoint.getJSONObject("body");

        // Final path construction
        String finalPath = pathTemplate
                .replace("{projectId}", projectId)
                .replace("{templateId}", templateId);

        // Send API request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .post();

        // Log and assert
        System.out.println("📡 Request Path: " + finalPath);
        System.out.println("✅ Status Code: " + response.getStatusCode());

        response.prettyPrint();

        assertEquals(response.getStatusCode(), expectedStatusCode, "❌ Failed to add component to library.");
    }
}
