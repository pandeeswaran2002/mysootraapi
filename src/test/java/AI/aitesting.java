package AI;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;

public class aitesting {

    @Test
    public void testAiTesting() {
        // Load data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("aiTesting");
        String pathTemplate = endpoint.getString("path");

        // Replace path params
        String projectId = endpoint.getJSONObject("params").getString("projectId");
        String templateId = endpoint.getJSONObject("params").getString("templateId");
        String versionId = endpoint.getJSONObject("params").getString("versionId");

        String finalPath = pathTemplate
                .replace("{projectId}", projectId)
                .replace("{templateId}", templateId)
                .replace("{versionId}", versionId);

        JSONObject body = endpoint.getJSONObject("body");

        // Send request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .multiPart("content", body.getString("content"))
                .multiPart("rec_doc", body.getString("rec_doc")) // Replace with actual file if needed
                .when()
                .post();

        int statusCode = response.getStatusCode();
        String statusSymbol = statusCode == 200 ? "✅" : "❌";
        String errorCode = "";

        // Try to parse error code/message from response
        try {
            JSONObject responseJson = new JSONObject(response.getBody().asString());
            if (responseJson.has("errorCode")) {
                errorCode = responseJson.getString("errorCode");
            } else if (responseJson.has("error")) {
                errorCode = responseJson.getString("error");
            } else if (responseJson.has("message")) {
                errorCode = responseJson.getString("message");
            }
        } catch (Exception e) {
            // Ignore if response is not JSON
        }

        // Log response
        System.out.println(statusSymbol + " Status Code: " + statusCode);
        if (!errorCode.isEmpty()) {
            System.out.println("🔴 Error Code/Message: " + errorCode);
        }

        response.prettyPrint();

        assertEquals(statusCode, 200, statusSymbol + " AI Testing API failed. Status Code: " + statusCode +
                (errorCode.isEmpty() ? "" : ", Error: " + errorCode));
    }
}
