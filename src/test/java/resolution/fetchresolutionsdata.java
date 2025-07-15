package resolution;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class fetchresolutionsdata {

    @Test
    public void testGetResolutions() {
        // 🔹 Load data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");  // Assuming it's a protected route

        JSONObject endpoint = json.getJSONObject("getResolutions");
        String method = endpoint.getString("method");
        String path = endpoint.getString("path");

        // 🔹 Send GET request
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .request(method);

        // 🔹 Output & Assert
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Failed to fetch resolutions.");
    }
}
