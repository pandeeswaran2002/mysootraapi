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

public class updatethetemplate {

    @Test
    public void testUpdateProjectTemplate() {
        // Load JSON configuration
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonData = new JSONObject(new JSONTokener(is));

        String baseUri = jsonData.getString("baseUri");
        String token = jsonData.getString("token");
        JSONObject config = jsonData.getJSONObject("updateProjectTemplate");

        JSONObject requestBody = config.getJSONObject("requestBody");

        // Send PUT request
        Response response = given()
                .baseUri(baseUri)
                .basePath(config.getString("basePath"))
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .put();

        response.prettyPrint();

        // Assertion
        assertEquals(response.getStatusCode(), config.getInt("expectedStatusCode"), "❌ Template update failed.");
    }
}
