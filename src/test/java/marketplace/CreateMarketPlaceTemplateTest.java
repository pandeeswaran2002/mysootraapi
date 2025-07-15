package marketplace;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.File;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import io.qameta.allure.Allure;

public class CreateMarketPlaceTemplateTest {

    @Test
    public void testCreateMarketPlaceTemplate() {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));
        JSONObject endpoint = json.getJSONObject("createMarketPlaceTemplate");

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");
        String path = endpoint.getString("path");
        JSONObject form = endpoint.getJSONObject("form");

        // Use file path from JSON directly (absolute or relative)
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)
                .multiPart("name", form.getString("name"))
                .multiPart("description", form.getString("description"))
                .multiPart("type", form.getString("type"))
                .multiPart("status", form.getString("status"))
                .multiPart("source", form.getString("source"))
                .multiPart("content", form.getString("content"))
                .multiPart("fileUpload", new File(form.getString("fileUpload"))) // 👈 directly here
                .when()
                .post();

        // Validate response
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Template creation failed.");
    }
}
