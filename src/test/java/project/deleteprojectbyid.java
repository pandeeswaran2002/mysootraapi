package project;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class deleteprojectbyid {

    @Test
    public void testDeleteProject() {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject data = new JSONObject(new JSONTokener(is));

        String baseUri = data.getString("baseUri");
        String token = data.getString("token");
        JSONObject project = data.getJSONObject("deleteProject");

        RestAssured.baseURI = baseUri;

        // Send DELETE request
        Response response = given()


                .header("Authorization", "Bearer " + token)
                .when()
                .delete(project.getString("path").replace("{projectId}", project.getString("projectId")));

        // Output response
        response.prettyPrint();

        // Assertions
        assertEquals(response.getStatusCode(), 200, "❌ Expected HTTP 200 OK for successful deletion.");

    }
}
