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

public class gettaggedcomments {

    @Test
    public void testGetTaggedComments() {
        // 🔹 Load JSON config
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        // 🔹 Get endpoint
        JSONObject endpoint = json.getJSONObject("getTaggedComments");
        String method = endpoint.getString("method");
        String path = endpoint.getString("path");

        // 🔹 Send request
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .request(method);

        // 🔹 Print and assert
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Failed to fetch tagged comments.");
    }
}
