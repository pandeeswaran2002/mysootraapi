package authentication;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import java.io.InputStream;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;
public class LoginTest {


    @Test
    public void testLoginWithValidCredentials() {
        // Load JSON file
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonFile = new JSONObject(new JSONTokener(is));

        // Extract login info
        String baseUri = jsonFile.getString("baseUri");
        JSONObject loginData = jsonFile.getJSONObject("login");
        String basePath = loginData.getString("basePath");
        int expectedStatus = loginData.getInt("expectedStatusCode");
        JSONObject body = loginData.getJSONObject("body");

        // Send POST request
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

        // 👉 Allure Attachments
        Allure.addAttachment("🔼 Request Body", "application/json", body.toString(), ".json");
        Allure.addAttachment("🔽 Response Body", "application/json", response.asPrettyString(), ".json");
        Allure.addAttachment("📦 Status Code", String.valueOf(actualStatus));

        // Log response
        response.prettyPrint();
        System.out.println(statusSymbol + " Status Code: " + actualStatus);

        // ✅ Only assert status code
        assertEquals(actualStatus, expectedStatus, statusSymbol + " Unexpected status code");

        // Optional: Extract token if needed
        try {
            JSONObject responseJson = new JSONObject(response.getBody().asString());
            String token = responseJson.optJSONObject("data") != null ?
                    responseJson.getJSONObject("data").optString("accessToken", "null") : "null";

            System.out.println("🔑 Access Token: " + token);
        } catch (Exception e) {
            System.out.println("❌ Response not in JSON format.");
            Allure.addAttachment("❌ Invalid Response", response.asString());
            throw e;
        }

        // Optional: Allure trace with Hamcrest for structure validation
        response.then()
                .statusCode(expectedStatus)
                .body("data.accessToken", notNullValue());
    }
}
