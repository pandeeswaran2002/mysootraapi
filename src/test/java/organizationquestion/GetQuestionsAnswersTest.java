package organizationquestion;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import io.qameta.allure.Allure;
public class GetQuestionsAnswersTest {

    @Test
    public void testFetchQuestionsAndAnswers() {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));
        JSONObject endpoint = json.getJSONObject("getQuestionsAnswers");

        String baseUri = json.getString("baseUri");
        String path = endpoint.getString("path");
        String token = json.getString("token");
        JSONObject queryParams = endpoint.getJSONObject("queryParams");

        // Send request with query params
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)
                .queryParam("page", queryParams.getInt("page"))
                .queryParam("pageSize", queryParams.getInt("pageSize"))
                .queryParam("search", queryParams.getString("search"))
                .when()
                .get();

        // Print response
        response.prettyPrint();

        // Assertions
        assertEquals(response.getStatusCode(), 200, "❌ Expected 200 OK");

    }
}
