package project;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.Iterator;
import io.qameta.allure.Allure;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class getprojects {

    @Test
    public void testGetProjects() {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject data = new JSONObject(new JSONTokener(is));

        String baseUri = data.getString("baseUri");
        String token = data.getString("token");
        JSONObject endpoint = data.getJSONObject("getProjects");
        JSONObject queryParams = endpoint.getJSONObject("queryParams");

        RestAssured.baseURI = baseUri;

        io.restassured.specification.RequestSpecification request = given()
                .header("Authorization", "Bearer " + token);

        // Add query parameters
        Iterator<String> keys = queryParams.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            request.queryParam(key, queryParams.getString(key));
        }

        // Send GET request
        Response response = request.get(endpoint.getString("path"));

        // Print and validate response
        response.prettyPrint();
        assertEquals(response.statusCode(), 200, "❌ Expected HTTP 200 OK");


    }
}
