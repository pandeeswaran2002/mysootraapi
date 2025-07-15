package mailgun;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;
import io.qameta.allure.Allure;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class getmailgunpreview {

    @Test
    public void testGetMailgunPreviewById() {
        // 🔹 Load JSON config
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("getMailgunPreview");
        String method = endpoint.getString("method");
        String pathTemplate = endpoint.getString("path");

        // 🔹 Extract test ID and construct full path
        String testId = endpoint.getJSONObject("params").getString("testId");
        String finalPath = pathTemplate.replace("{testId}", testId);

        // 🔹 Send GET request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .request(method);

        // 🔹 Print and assert
        response.prettyPrint();
        assertEquals(response.statusCode(), 200, "❌ Failed to fetch Mailgun preview.");
    }
}
