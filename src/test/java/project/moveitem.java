package project;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;
import io.qameta.allure.Allure;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class moveitem {

    @Test
    public void testMoveItemsToFolder() {
        // Load test data from data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject data = new JSONObject(new JSONTokener(is));
        String baseUri = data.getString("baseUri");
        String token = data.getString("token");
        JSONObject moveData = data.getJSONObject("moveItems");

        RestAssured.baseURI = baseUri;

        // Send POST request
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(moveData.getJSONObject("payload").toString())
                .post(moveData.getString("path"));

        // Print response for debugging
        response.prettyPrint();

        // Assert status
        assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");


    }
}
