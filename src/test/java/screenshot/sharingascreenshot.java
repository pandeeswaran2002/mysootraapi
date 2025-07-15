package screenshot;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class sharingascreenshot {

    @Test
    public void testShareScreenshot() {
        // Load JSON
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("shareScreenshot");
        String method = endpoint.getString("method");
        String pathTemplate = endpoint.getString("path");
        String screenshotId = endpoint.getJSONObject("params").getString("screenshotId");
        JSONObject body = endpoint.getJSONObject("body");

        String finalPath = pathTemplate.replace("{screenshotId}", screenshotId);

        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .request(method);

        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Screenshot sharing failed.");
    }
}
