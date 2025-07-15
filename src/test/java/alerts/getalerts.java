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

public class getalerts {

    @Test
    public void testGetAlerts() {
        // Load Data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonData = new JSONObject(new JSONTokener(is));

        String baseUri = jsonData.getString("baseUri");
        String token = jsonData.getString("token");

        JSONObject alertData = jsonData.getJSONObject("getAlerts");
        JSONObject queryParams = alertData.getJSONObject("queryParams");
        int expectedStatusCode = alertData.getInt("expectedStatusCode");

        // Send GET request
        Response response = given()
                .baseUri(baseUri)
                .basePath(alertData.getString("basePath"))
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .queryParam("page", queryParams.getInt("page"))
                .queryParam("pageSize", queryParams.getInt("pageSize"))
                .when()
                .get();

        int actualStatusCode = response.getStatusCode();
        String statusSymbol = actualStatusCode == expectedStatusCode ? "✅" : "❌";
        String errorCode = "";

        // Attempt to extract error code or message if available
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
            // Not a JSON response or no error field
        }

        // Output results
        System.out.println(statusSymbol + " Status Code: " + actualStatusCode);
        if (!errorCode.isEmpty()) {
            System.out.println("🔴 Error Code/Message: " + errorCode);
        }

        response.prettyPrint();

        assertEquals(actualStatusCode, expectedStatusCode,
                statusSymbol + " Failed to fetch alerts. Status Code: " + actualStatusCode +
                        (errorCode.isEmpty() ? "" : ", Error: " + errorCode));
    }
}
