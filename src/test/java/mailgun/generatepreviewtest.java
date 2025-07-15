package mailgun;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import io.qameta.allure.Allure;

public class generatepreviewtest {

    @Test
    public void testGenerateMailgunPreview() {
        // 🔹 Load JSON
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("generateMailgunPreview");
        String method = endpoint.getString("method");
        String path = endpoint.getString("path");
        JSONObject body = endpoint.getJSONObject("body");

        // 🔹 Send POST request
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .request(method);

        // 🔹 Output and validate
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Failed to generate Mailgun preview.");
    }
}
