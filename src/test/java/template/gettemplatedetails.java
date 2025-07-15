package template;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class gettemplatedetails {

    @Test
    public void testGetProjectTemplateById() {
        // Load JSON config
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonData = new JSONObject(new JSONTokener(is));

        String baseUri = jsonData.getString("baseUri");
        String token = jsonData.getString("token");
        JSONObject config = jsonData.getJSONObject("getProjectTemplateById");

        // Send GET request
        Response response = given()
                .baseUri(baseUri)
                .basePath(config.getString("basePath"))
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .get();

        response.prettyPrint();

        // Validate response
        assertEquals(response.getStatusCode(), config.getInt("expectedStatusCode"), "❌ Failed to fetch template details by ID.");
    }
}
