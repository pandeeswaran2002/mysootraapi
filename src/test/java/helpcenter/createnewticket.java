package helpcenter;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.File;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import io.qameta.allure.Allure;

public class createnewticket {

    @Test
    public void testCreateIssueTicket() {
        // Load test data from JSON
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("createIssueTicket");
        String path = endpoint.getString("path");
        JSONObject formData = endpoint.getJSONObject("formData");
        String filePath = endpoint.getString("filePath");

        // Send POST request
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)
                .multiPart("issueDescription", formData.getString("issueDescription"))
                .multiPart("name", formData.getString("name"))
                .multiPart("email", formData.getString("email"))
                .multiPart("phoneNumber", formData.getString("phoneNumber"))
                .multiPart("attachment", new File(filePath))
                .when()
                .post();

        // Output and validate
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "❌ Issue ticket creation failed.");
    }
}
