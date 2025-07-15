package alerts;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;
import io.qameta.allure.Allure;

public class updatealertbyid {

    @Test
    public void testUpdateAlert() {
        // Load JSON
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonData = new JSONObject(new JSONTokener(is));

        String baseUri = jsonData.getString("baseUri");
        String token = jsonData.getString("token");

        JSONObject alertData = jsonData.getJSONObject("updateAlert");
        String alertId = alertData.getString("alertId");
        JSONObject body = alertData.getJSONObject("body");
        int expectedStatusCode = alertData.getInt("expectedStatusCode");

        // Make the PUT request
        Response response = given()
                .baseUri(baseUri)
                .basePath(alertData.getString("basePath") + "/" + alertId)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .put();

        int actualStatusCode = response.getStatusCode();
        String statusSymbol = actualStatusCode == expectedStatusCode ? "✅" : "❌";
        String errorCode = "";

        // Try extracting error details from response
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
            // Non-JSON response; do nothing
        }

        // Output results
        System.out.println(statusSymbol + " Status Code: " + actualStatusCode);
        if (!errorCode.isEmpty()) {
            System.out.println("🔴 Error Code/Message: " + errorCode);
        }

        response.prettyPrint();

        assertEquals(actualStatusCode, expectedStatusCode,
                statusSymbol + " Alert update failed. Status Code: " + actualStatusCode +
                        (errorCode.isEmpty() ? "" : ", Error: " + errorCode));
    }
}
