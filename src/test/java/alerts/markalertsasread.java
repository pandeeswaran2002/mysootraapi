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

public class markalertsasread {

    @Test
    public void testMarkAlertsAsRead() {
        // Load JSON
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonData = new JSONObject(new JSONTokener(is));

        String baseUri = jsonData.getString("baseUri");
        String token = jsonData.getString("token");

        JSONObject alertData = jsonData.getJSONObject("markAlertsAsRead");
        int expectedStatusCode = alertData.getInt("expectedStatusCode");

        // Send POST request
        Response response = given()
                .baseUri(baseUri)
                .basePath(alertData.getString("basePath"))
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .post();

        int actualStatusCode = response.getStatusCode();
        String statusSymbol = actualStatusCode == expectedStatusCode ? "✅" : "❌";
        String errorMessage = "";

        // Try to extract error details from response
        try {
            JSONObject responseJson = new JSONObject(response.getBody().asString());
            if (responseJson.has("errorCode")) {
                errorMessage = responseJson.getString("errorCode");
            } else if (responseJson.has("error")) {
                errorMessage = responseJson.getString("error");
            } else if (responseJson.has("message")) {
                errorMessage = responseJson.getString("message");
            }
        } catch (Exception e) {
            // Possibly non-JSON response
        }

        // Print status
        System.out.println(statusSymbol + " Status Code: " + actualStatusCode);
        if (!errorMessage.isEmpty()) {
            System.out.println("🔴 Error Code/Message: " + errorMessage);
        }

        response.prettyPrint();

        // Final assertion
        assertEquals(actualStatusCode, expectedStatusCode,
                statusSymbol + " Failed to mark alerts as read. Status Code: " + actualStatusCode +
                        (errorMessage.isEmpty() ? "" : ", Error: " + errorMessage));
    }
}
