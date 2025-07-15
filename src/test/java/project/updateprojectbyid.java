package project;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.File;
import java.io.InputStream;
import io.qameta.allure.Allure;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class updateprojectbyid {

    @Test
    public void testUpdateProjectById() {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject data = new JSONObject(new JSONTokener(is));

        String baseUri = data.getString("baseUri");
        String token = data.getString("token");
        JSONObject project = data.getJSONObject("updateProject");

        RestAssured.baseURI = baseUri;

        // Replace {projectId} in path
        String path = project.getString("path").replace("{projectId}", project.getString("projectId"));

        // Send PUT request
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .multiPart("name", project.getString("name"))
                .multiPart("description", project.getString("description"))
                .multiPart("color", project.getString("color"))
                .multiPart("users", project.getString("users").toString())
                .when()
                .put(path);

        // Output response
        response.prettyPrint();

        // Validate response
        assertEquals(response.getStatusCode(), 200, "❌ Expected HTTP 200 OK");
    }
}