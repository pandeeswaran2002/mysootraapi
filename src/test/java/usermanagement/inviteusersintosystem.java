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

public class inviteusersintosystem {

    @Test
    public void testInviteUsers() {
        // Load shared JSON config
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonData = new JSONObject(new JSONTokener(is));

        // Extract common + endpoint-specific values
        String baseUri = jsonData.getString("baseUri");
        String token = jsonData.getString("token");
        JSONObject inviteData = jsonData.getJSONObject("inviteUsersIntoSystem");

        String basePath = inviteData.getString("basePath");
        int expectedStatusCode = inviteData.getInt("expectedStatusCode");
        JSONObject body = inviteData.getJSONObject("body");

        // Perform POST request
        Response response = given()
                .baseUri(baseUri)
                .basePath(basePath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .post();

        response.prettyPrint();
        assertEquals(response.getStatusCode(), expectedStatusCode, "❌ Status code mismatch.");
    }
}
