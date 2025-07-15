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

public class fetchthescreenshot {

    @Test
    public void testFetchScreenshotFromJson() {
        // 🔹 Load JSON from resources
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        JSONObject endpoint = json.getJSONObject("getPublicScreenshot");

        String method = endpoint.getString("method");
        String pathTemplate = endpoint.getString("path");
        String screenshotId = endpoint.getJSONObject("params").getString("screenshotId");

        String finalPath = pathTemplate.replace("{screenshotId}", screenshotId);

        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .contentType(ContentType.JSON)
                .when()
                .request(method);

        response.prettyPrint();

        assertEquals(response.getStatusCode(), 200, "❌ Failed to fetch public screenshot.");
    }
}
