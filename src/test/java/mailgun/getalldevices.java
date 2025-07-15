package mailgun;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import io.qameta.allure.Allure;

public class getalldevices {

    @Test
    public void testGetAllDevices() {
        // 🔹 Load data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        // 🔹 Extract common data
        String baseUri = json.getString("baseUri");
        String token = json.getString("token");  // Ensure this exists in your data.json

        JSONObject endpoint = json.getJSONObject("getAllDevices");
        String method = endpoint.getString("method");
        String path = endpoint.getString("path");

        // 🔹 Prepare query parameters
        Map<String, String> queryParams = new HashMap<>();
        if (endpoint.has("query")) {
            JSONObject query = endpoint.getJSONObject("query");
            Iterator<String> keys = query.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                queryParams.put(key, query.getString(key));
            }
        }

        // 🔹 Send request
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token) // 🔐 Auth token added
                .contentType(ContentType.JSON)
                .queryParams(queryParams)
                .when()
                .request(method);

        // 🔹 Output & validate
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Failed to fetch devices.");
    }
}
