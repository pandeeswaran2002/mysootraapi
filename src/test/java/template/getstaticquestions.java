package template;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class getstaticquestions {

    @Test
    public void testFetchStaticQuestions() {
        // Load JSON data
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonData = new JSONObject(new JSONTokener(is));

        String baseUri = jsonData.getString("baseUri");
        String token = jsonData.getString("token");
        JSONObject config = jsonData.getJSONObject("getStaticQuestions");

        // Send GET request
        Response response = given()
                .baseUri(baseUri)
                .basePath(config.getString("basePath"))
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .get();

        response.prettyPrint();

        // Assertion
        assertEquals(response.getStatusCode(), config.getInt("expectedStatusCode"), "❌ Failed to fetch static questions.");
    }
}
