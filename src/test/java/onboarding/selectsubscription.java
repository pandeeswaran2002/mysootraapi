package onboarding;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class selectsubscription {

    public JSONObject loadConfig() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        if (is == null) throw new RuntimeException("❌ Data.json not found.");
        return new JSONObject(new JSONTokener(is));
    }

    @Test
    public void testSelectPlan() {


        JSONObject jsonData = loadConfig(); // ✅ Load full JSON
        JSONObject inviteData = jsonData.getJSONObject("selectSubscription");

        // ✅ Extract values
        String baseUri = jsonData.getString("baseUri");
        String basePath = inviteData.getString("basePath");
        String token = jsonData.getString("token");
        int expectedStatusCode = inviteData.getInt("expectedStatusCode");

        JSONObject requestBody = inviteData.getJSONObject("body");

        // ✅ Execute request
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
        assertEquals(response.getStatusCode(), expectedStatusCode, "❌ Status code mismatch");

        System.out.println("✅ Subscription selection test passed.");
    }
}
