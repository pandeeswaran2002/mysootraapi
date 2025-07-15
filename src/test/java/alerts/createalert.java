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

public class createalert {

    @Test
    public void testCreateAlert() {
        // Load Data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonData = new JSONObject(new JSONTokener(is));

        String baseUri = jsonData.getString("baseUri");
        String token = jsonData.getString("token");

        JSONObject alertData = jsonData.getJSONObject("createAlert");
        int expectedStatusCode = alertData.getInt("expectedStatusCode");

        Response response = given()
                .baseUri(baseUri)
                .basePath(alertData.getString("basePath"))
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(alertData.getJSONObject("body").toString())
                .when()
                .post();

        int actualStatusCode = response.getStatusCode();
        String statusSymbol = actualStatusCode == expectedStatusCode ? "✅" : "❌";
        String errorCode = "";

        // Try to extract error message/code
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
            // Response body not JSON or no error key found
        }

        // Log result
        System.out.println(statusSymbol + " Status Code: " + actualStatusCode);
        if (!errorCode.isEmpty()) {
            System.out.println("🔴 Error Code/Message: " + errorCode);
        }

        response.prettyPrint();

        assertEquals(actualStatusCode, expectedStatusCode,
                statusSymbol + " Alert creation failed. Status Code: " + actualStatusCode +
                        (errorCode.isEmpty() ? "" : ", Error: " + errorCode));
    }
}
