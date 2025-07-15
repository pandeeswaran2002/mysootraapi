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

public class inviteuser {

    public JSONObject loadConfig() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        if (is == null) throw new RuntimeException("❌ Failed to load Data.json");
        return new JSONObject(new JSONTokener(is));
    }

    @Test
    public void testInviteUsers() {
        JSONObject jsonData = loadConfig(); // ✅ Load full JSON
        JSONObject inviteData = jsonData.getJSONObject("onboardingInvite");

        // ✅ Extract fields
        String baseUri = jsonData.getString("baseUri");
        String basePath = inviteData.getString("basePath");
        String token = jsonData.getString("token");
        int expectedStatusCode = inviteData.getInt("expectedStatusCode");

        JSONObject requestBody = inviteData.getJSONObject("body");

        // ✅ Perform request
        Response response = given()
                .baseUri(baseUri)
                .basePath(basePath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .post();

        // ✅ Logging & Assertions
        response.prettyPrint();
        assertEquals(response.getStatusCode(), expectedStatusCode, "❌ Unexpected status code");

        System.out.println("✅ Invite user test passed.");
    }
}
