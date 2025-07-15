package contactusform;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;
import io.qameta.allure.Allure;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class contactusform {

    @Test
    public void testContactUsForm() {
        // 🔹 Load data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        JSONObject endpoint = json.getJSONObject("contactUs");

        String method = endpoint.getString("method");
        String path = endpoint.getString("path");
        JSONObject body = endpoint.getJSONObject("body");

        // 🔹 Send POST request
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .request(method);

        // 🔹 Print and assert
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 201, "❌ Contact Us form submission failed.");
    }
}
