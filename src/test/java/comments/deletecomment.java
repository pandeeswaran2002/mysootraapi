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

public class deletecomment {

    @Test
    public void testDeleteComment() {
        // 🔹 Load JSON config
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        // 🔹 Extract endpoint details
        JSONObject endpoint = json.getJSONObject("deleteComment");
        String commentId = endpoint.getJSONObject("params").getString("commentId");
        String finalPath = endpoint.getString("path").replace("{commentId}", commentId);

        // 🔹 Send DELETE request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .delete();

        // 🔹 Print & Assert
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Failed to delete comment.");
    }
}
