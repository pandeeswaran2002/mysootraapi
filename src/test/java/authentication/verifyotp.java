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
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;

public class verifyotp {

    @Test
    public void testVerifyOtp() {
        // Load JSON config
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        // Extract global and local data
        String baseUri = json.getString("baseUri");
        JSONObject verifyOtpData = json.getJSONObject("verifyotp");

        String basePath = verifyOtpData.getString("basePath");
        int expectedStatus = verifyOtpData.getInt("expectedStatusCode");
        String expectedMessage = verifyOtpData.getString("expectedMessage");
        JSONObject body = verifyOtpData.getJSONObject("body");

        // Perform API request
        Response response = RestAssured
                .given()
                .baseUri(baseUri)
                .basePath(basePath)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .post();

        // Output raw response for debug
        response.prettyPrint();

        // Parse response body
        JSONObject resJson = new JSONObject(response.getBody().asString());
        String message = resJson.optString("message", "N/A");
        String errorCode = resJson.optString("errorCode", "None");

        // 🧾 Log response fields
        System.out.println("✅ Status Code: " + response.getStatusCode());
        System.out.println("📨 Message: " + message);
        System.out.println("🧾 Error Code: " + errorCode);

        // ✅ Assert status code and message
        assertEquals(response.getStatusCode(), expectedStatus, "❌ Status code mismatch");
        assertEquals(message, expectedMessage, "❌ Message mismatch");

        // Optional Contract Test
        response.then()
                .statusCode(expectedStatus)
                .body("message", equalTo(expectedMessage));
    }
}
