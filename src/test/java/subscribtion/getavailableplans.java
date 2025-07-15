package subscribtion;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class getavailableplans {

    @Test
    public void testRetrieveAvailablePlans() {
        // Load data from JSON
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assertNotNull(is, "data.json not found");
        JSONObject data = new JSONObject(new JSONTokener(is));

        String baseUri = data.getString("baseUri");
        String token = data.getString("token"); // If needed
        JSONObject planData = data.getJSONObject("getPlans");
        JSONObject queryParams = planData.getJSONObject("queryParams");

        RestAssured.baseURI = baseUri;

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .queryParam("page", queryParams.getInt("page"))
                .queryParam("pageSize", queryParams.getInt("pageSize"))
                .get(planData.getString("path"));

        response.prettyPrint();

        // Basic assertions
        assertEquals(response.statusCode(), 200, "Expected HTTP status code 200");

    }
}
