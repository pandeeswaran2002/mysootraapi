package template;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class gettemplatestatus {

    @Test
    public void testGetTemplateStatusWithType() {
        // Load config from Data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonData = new JSONObject(new JSONTokener(is));

        String baseUri = jsonData.getString("baseUri");
        String token = jsonData.getString("token");

        // Optional query param: email / form / landingPage
        String templateType = "email";

        // Send GET request
        Response response = given()
                .baseUri(baseUri)
                .basePath("/projects/templates/status")
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .queryParam("templateType", templateType)
                .when()
                .get();

        response.prettyPrint();

        // Validate response
        assertEquals(response.getStatusCode(), 200, "❌ Failed to fetch template status and usage info.");
    }
}
