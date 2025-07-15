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

public class getalltickets {

    @Test
    public void testGetAllIssueTickets() {
        // Load test data from JSON
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");
        JSONObject endpoint = json.getJSONObject("getAllIssueTickets");
        String path = endpoint.getString("path");

        // Send GET request
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)
                .when()
                .get();

        // Output and assert
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Failed to retrieve issue tickets.");
    }
}
