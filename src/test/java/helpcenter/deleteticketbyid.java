package helpcenter;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import io.qameta.allure.Allure;

public class deleteticketbyid {

    @Test
    public void testDeleteIssueTicket() {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");
        JSONObject endpoint = json.getJSONObject("deleteIssueTicket");
        String path = endpoint.getString("path");

        // Send DELETE request
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete();

        // Output and assert
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Ticket deletion failed.");
    }
}
