package screenshot;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.File;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class createscreenshot {

    @Test
    public void testUploadScreenshot() {
        // Load Data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonData = new JSONObject(new JSONTokener(is));

        String baseUri = jsonData.getString("baseUri");
        String token = jsonData.getString("token");

        JSONObject screenshotData = jsonData.getJSONObject("uploadScreenshot");

        String method = screenshotData.getString("method");
        String path = screenshotData.getString("path");

        JSONObject attachment = screenshotData.getJSONObject("attachment");
        String filePath = attachment.getString("filePath");
        String contentType = attachment.getString("contentType");

        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("❌ File not found: " + filePath);
        }

        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)
                .multiPart("attachment", file, contentType)
                .when()
                .request(method);

        response.prettyPrint();

        assertEquals(response.getStatusCode(), screenshotData.getInt("expectedStatusCode"), "❌ Screenshot upload failed.");
    }
}
