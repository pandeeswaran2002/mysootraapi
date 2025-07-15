package AI;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;

public class aiImage {

    @Test
    public void testAiImageApi() {
        // Load data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("aiImage");
        String pathTemplate = endpoint.getString("path");

        // Replace path parameters
        String projectId = endpoint.getJSONObject("params").getString("projectId");
        String templateId = endpoint.getJSONObject("params").getString("templateId");
        String versionId = endpoint.getJSONObject("params").getString("versionId");

        String finalPath = pathTemplate
                .replace("{projectId}", projectId)
                .replace("{templateId}", templateId)
                .replace("{versionId}", versionId);

        JSONObject requestBody = new JSONObject();
        JSONArray urls = endpoint.getJSONObject("body").getJSONArray("imageUrls");
        requestBody.put("imageUrls", urls);

        // Send request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post();

        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        String statusSymbol = statusCode == 200 ? "✅" : "❌";

        // Try to extract error code/message if available
        String errorCode = "";
        try {
            JSONObject responseJson = new JSONObject(responseBody);
            if (responseJson.has("errorCode")) {
                errorCode = responseJson.getString("errorCode");
            } else if (responseJson.has("error")) {
                errorCode = responseJson.getString("error");
            }
        } catch (Exception e) {
            // response body is not JSON or doesn't contain error info
        }

        // Output and validate
        System.out.println(statusSymbol + " Status Code: " + statusCode);
        if (!errorCode.isEmpty()) {
            System.out.println("🔴 Error Code: " + errorCode);
        }
        response.prettyPrint();

        assertEquals(statusCode, 200, statusSymbol + " AI Image API failed. Status Code: " + statusCode +
                (errorCode.isEmpty() ? "" : ", Error Code: " + errorCode));
    }
}
