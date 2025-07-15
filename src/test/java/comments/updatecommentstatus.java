package comments;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class updatecommentstatus {

    @Test
    public void testUpdateCommentStatus() {
        // 🔹 Load JSON configuration
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        // 🔹 Extract endpoint details
        JSONObject endpoint = json.getJSONObject("updateCommentStatus");
        String commentId = endpoint.getJSONObject("params").getString("commentId");
        JSONObject body = endpoint.getJSONObject("body");

        String finalPath = endpoint.getString("path").replace("{commentId}", commentId);

        // 🔹 Send PUT request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .put();

        // 🔹 Output & assertion
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Comment status update failed.");
    }
}
