package authentication;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;



import java.io.InputStream;

import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

public class signup {

    @Test
    public void testSignupWithValidCredentials() {
        // Load Data.json from resources
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonFile = new JSONObject(new JSONTokener(is));

        // Extract shared baseUri
        String baseUri = jsonFile.getString("baseUri");

        // Extract "signup" block
        JSONObject signupData = jsonFile.getJSONObject("signup");
        String basePath = signupData.getString("basePath");
        int expectedStatus = signupData.getInt("expectedStatusCode");
        String expectedMessage = signupData.getString("expectedMessage");
        JSONObject body = signupData.getJSONObject("body");

        // Send request and capture response
        Response response = RestAssured
                .given()
                .baseUri(baseUri)
                .basePath(basePath)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .post();

        int actualStatus = response.statusCode();
        String statusSymbol = (actualStatus == expectedStatus) ? "✅" : "❌";

        // Log status code
        System.out.println(statusSymbol + " Status Code: " + actualStatus);

        // Attempt to parse and log response content
        try {
            JSONObject responseJson = new JSONObject(response.getBody().asString());

            String message = responseJson.optString("message", "No message returned");
            String token = responseJson.optJSONObject("data") != null ?
                    responseJson.getJSONObject("data").optString("accessToken", "null") : "null";

            System.out.println("📨 Message: " + message);
            System.out.println("🔑 Access Token: " + token);

            assertEquals(actualStatus, expectedStatus, statusSymbol + " Unexpected status code");
            assertEquals(message, expectedMessage, statusSymbol + " Unexpected message");

        } catch (Exception e) {
            System.out.println("❌ Failed to parse response as JSON.");
            throw e;
        }

        // Optional: print full response
        response.prettyPrint();

        // Final assertions using Hamcrest for detailed checks
        response.then()
                .statusCode(expectedStatus)
                .body("message", equalTo(expectedMessage))
                .body("data.accessToken", notNullValue());
    }
}
