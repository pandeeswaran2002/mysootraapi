package project;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class createprojects {

    @Test
    public void testCreateProject() {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject data = new JSONObject(new JSONTokener(is));

        String baseUri = data.getString("baseUri");
        String token = data.getString("token");

        JSONObject endpoint = data.getJSONObject("createProject");
        JSONObject formParams = endpoint.getJSONObject("formParams");
        JSONObject fileParam = endpoint.getJSONObject("fileParam");

        // Build request
        RestAssured.baseURI = baseUri;

        io.restassured.specification.RequestSpecification request = given()
                .multiPart("name", formParams.getString("name"))
                .multiPart("description", formParams.getString("description"))
                .multiPart("color", formParams.getString("color"))
                .multiPart("isDefaultPermission", formParams.getString("isDefaultPermission"))
                .header("Authorization", "Bearer " + token);

        // Add each user ID
        for (Object user : formParams.getJSONArray("users")) {
            request.multiPart("users", user.toString());
        }

        // Optional file
        if (!fileParam.isEmpty()) {
            File file = new File(fileParam.getString("path"));
            if (file.exists()) {
                request.multiPart(fileParam.getString("field"), file);
            }
        }

        // Send request
        Response response = request.post(endpoint.getString("path"));

        // Output and assertion
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Project creation failed");
    }
}
