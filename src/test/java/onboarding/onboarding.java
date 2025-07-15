package onboarding;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class onboarding {

    public JSONObject loadConfig() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        return new JSONObject(new JSONTokener(is));
    }

    @Test
    public void testPostOnboardingDetails() {
        JSONObject jsonData = loadConfig(); // ✅ Load full JSON

        // ✅ Read onboarding section
        JSONObject onboarding = jsonData.getJSONObject("onboarding");

        // ✅ Extract required fields
        String baseUri = jsonData.getString("baseUri"); // Centralized
        String basePath = onboarding.getString("basePath");
        int expectedStatusCode = onboarding.getInt("expectedStatusCode");
        String expectedMessage = onboarding.getString("expectedMessage");
        String token = jsonData.getString("token");
        JSONObject requestBody = onboarding.getJSONObject("body");

        // ✅ Perform POST request
        Response response = given()
                .baseUri(baseUri)
                .basePath(basePath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .post();

        // ✅ Output & assertions
        response.prettyPrint();
        assertEquals(response.getStatusCode(), expectedStatusCode, "❌ Unexpected status code");
        assertEquals(response.jsonPath().getString("message"), expectedMessage, "❌ Message mismatch");
        System.out.println("✅ Onboarding data submitted successfully.");
    }
}
