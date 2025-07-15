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

public class getreviewersbystatus {

    @Test
    public void testGetReviewersByVersion() {
        // 🔹 Load data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("getReviewersByVersion");
        String method = endpoint.getString("method");
        String pathTemplate = endpoint.getString("path");

        // 🔹 Replace path params
        JSONObject params = endpoint.getJSONObject("params");
        String finalPath = pathTemplate
                .replace("{projectId}", params.getString("projectId"))
                .replace("{templateId}", params.getString("templateId"))
                .replace("{versionId}", params.getString("versionId"));

        // 🔹 Send GET request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .request(method);

        // 🔹 Print and validate
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Failed to fetch reviewer statuses.");
    }
}
