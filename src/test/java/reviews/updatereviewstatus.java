package reviews;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class updatereviewstatus {

    @Test
    public void testUpdateReviewStatus() {
        // 🔹 Load data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        // 🔹 Extract endpoint
        JSONObject endpoint = json.getJSONObject("updateReviewStatus");
        String method = endpoint.getString("method");
        String pathTemplate = endpoint.getString("path");

        // 🔹 Replace path parameters
        JSONObject params = endpoint.getJSONObject("params");
        String finalPath = pathTemplate
                .replace("{projectId}", params.getString("projectId"))
                .replace("{templateId}", params.getString("templateId"))
                .replace("{versionId}", params.getString("versionId"));

        // 🔹 Prepare body
        JSONObject body = endpoint.getJSONObject("body");

        // 🔹 Send request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .request(method);

        // 🔹 Print response and validate
        response.prettyPrint();
        assertEquals(response.statusCode(), 200, "❌ Review update failed.");
    }
}
