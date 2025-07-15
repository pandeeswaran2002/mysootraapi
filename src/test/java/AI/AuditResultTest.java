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

public class AuditResultTest {

    @Test
    public void testAuditResult() {
        // Load data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("auditResult");
        String path = endpoint.getString("path");
        JSONObject params = endpoint.getJSONObject("params");

        // Replace path variables
        String finalPath = path
                .replace("{projectId}", params.getString("projectId"))
                .replace("{templateId}", params.getString("templateId"))
                .replace("{versionId}", params.getString("versionId"));

        // Send GET request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .when()
                .get();

        int statusCode = response.getStatusCode();
        String statusSymbol = statusCode == 200 ? "✅" : "❌";
        String errorCode = "";

        // Try to extract error message/code from response body
        try {
            JSONObject responseBody = new JSONObject(response.getBody().asString());
            if (responseBody.has("errorCode")) {
                errorCode = responseBody.getString("errorCode");
            } else if (responseBody.has("error")) {
                errorCode = responseBody.getString("error");
            } else if (responseBody.has("message")) {
                errorCode = responseBody.getString("message");
            }
        } catch (Exception e) {
            // Ignore if response body is not JSON
        }

        // Output results
        System.out.println(statusSymbol + " Status Code: " + statusCode);
        if (!errorCode.isEmpty()) {
            System.out.println("🔴 Error Code/Message: " + errorCode);
        }

        response.prettyPrint();

        assertEquals(statusCode, 200, statusSymbol + " Audit result fetch failed. Status Code: " + statusCode +
                (errorCode.isEmpty() ? "" : ", Error: " + errorCode));
    }
}
