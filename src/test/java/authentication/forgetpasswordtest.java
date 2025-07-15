package authentication;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;


import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

public class forgetpasswordtest {

    @Test
    public void testForgetPassword() {
        // Load JSON data from file
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonFile = new JSONObject(new JSONTokener(is));

        // Base URI and request data
        String baseUri = jsonFile.getString("baseUri");
        JSONObject forgetData = jsonFile.getJSONObject("forgetpassword");
        String basePath = forgetData.getString("basePath");
        int expectedStatus = forgetData.getInt("expectedStatusCode");
        String expectedMessage = forgetData.getString("expectedMessage");
        JSONObject body = forgetData.getJSONObject("body");

        // Send API request and capture response
        Response response = RestAssured
                .given()
                .baseUri(baseUri)
                .basePath(basePath)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .post();

        // Print full response
        response.prettyPrint();

        int actualStatus = response.statusCode();
        String statusIcon = (actualStatus == expectedStatus) ? "✅" : "❌";
        System.out.println(statusIcon + " Status Code: " + actualStatus);

        try {
            JSONObject resJson = new JSONObject(response.getBody().asString());
            String message = resJson.optString("message", "No message");
            String errorCode = resJson.optString("errorCode", "None");

            System.out.println("📨 Message: " + message);
            System.out.println("🧾 Error Code: " + errorCode);

            // Soft assertions
            assertEquals(actualStatus, expectedStatus, statusIcon + " Unexpected status code");
            assertEquals(message, expectedMessage, statusIcon + " Unexpected message");

        } catch (Exception e) {
            System.out.println("❌ Failed to parse response JSON.");
            throw e;
        }

        // Final API contract check
        response.then()
                .statusCode(expectedStatus)
                .body("message", equalTo(expectedMessage));
    }
}
