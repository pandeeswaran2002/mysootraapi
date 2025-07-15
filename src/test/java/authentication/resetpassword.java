package authentication;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import io.qameta.allure.Allure;
import org.testng.annotations.Test;


import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

public class resetpassword {

    @Test
    public void testResetPassword() {
        // Load the JSON config
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonFile = new JSONObject(new JSONTokener(is));

        // Extract API details
        String baseUri = jsonFile.getString("baseUri");
        JSONObject resetData = jsonFile.getJSONObject("resetpassword");
        String basePath = resetData.getString("basePath");
        int expectedStatus = resetData.getInt("expectedStatusCode");
        String expectedMessage = resetData.getString("expectedMessage");
        JSONObject body = resetData.getJSONObject("body");

        // Send request
        Response response = RestAssured
                .given()
                .baseUri(baseUri)
                .basePath(basePath)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .post();

        // Pretty print response
        response.prettyPrint();

        int actualStatus = response.getStatusCode();
        String statusIcon = (actualStatus == expectedStatus) ? "✅" : "❌";
        System.out.println(statusIcon + " Status Code: " + actualStatus);

        try {
            JSONObject responseJson = new JSONObject(response.getBody().asString());
            String message = responseJson.optString("message", "No message");
            String errorCode = responseJson.optString("errorCode", "None");

            System.out.println("📨 Message: " + message);
            System.out.println("🧾 Error Code: " + errorCode);

            assertEquals(actualStatus, expectedStatus, statusIcon + " Status code mismatch");
            assertEquals(message, expectedMessage, statusIcon + " Message mismatch");

        } catch (Exception e) {
            System.out.println("❌ Failed to parse response JSON.");
            throw e;
        }

        // Contract validation
        response.then()
                .statusCode(expectedStatus)
                .body("message", equalTo(expectedMessage));
    }
}
