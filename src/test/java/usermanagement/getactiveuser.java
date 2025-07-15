package usermanagement;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class getactiveuser {

    @Test
    public void testGetActiveUsers() {
        // Load shared JSON config
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonData = new JSONObject(new JSONTokener(is));

        String baseUri = jsonData.getString("baseUri");
        String token = jsonData.getString("token");

        JSONObject activeUserData = jsonData.getJSONObject("getActiveUser");
        String basePath = activeUserData.getString("basePath");
        String fromDate = activeUserData.optString("from", "");
        String toDate = activeUserData.optString("to", "");
        int expectedStatus = activeUserData.getInt("expectedStatusCode");

        // API call
        Response response = given()
                .baseUri(baseUri)
                .basePath(basePath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .queryParam("from", fromDate)
                .queryParam("to", toDate)
                .when()
                .get();

        response.prettyPrint();
        assertEquals(response.statusCode(), expectedStatus, "❌ Status Code mismatch.");
    }
}
