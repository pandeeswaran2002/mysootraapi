package shareaccess;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class sharefileaccess {

    @Test
    public void testShareAccessToFile() {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("shareAccessToFile");
        String path = endpoint.getString("path");
        JSONObject params = endpoint.getJSONObject("params");
        JSONObject body = endpoint.getJSONObject("body");

        // Replace placeholders in path
        String finalPath = path
                .replace("{projectId}", params.getString("projectId"))
                .replace("{fileId}", params.getString("fileId"));

        // Send PUT request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .put();

        // Debug print and assert
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ File access sharing failed.");
    }
}
