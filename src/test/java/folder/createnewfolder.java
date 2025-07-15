package folder;
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

public class createnewfolder {

    @Test
    public void testCreateFolder() {
        // Load data.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");

        JSONObject endpoint = json.getJSONObject("createFolder");
        String path = endpoint.getString("path");
        JSONObject params = endpoint.getJSONObject("params");
        JSONObject formData = endpoint.getJSONObject("formData");

        // Replace path variable
        String finalPath = path.replace("{workspaceId}", params.getString("workspaceId"));

        // File upload
        File file = new File(endpoint.getString("filePath"));

        // Send POST request
        Response response = given()
                .baseUri(baseUri)
                .basePath(finalPath)
                .header("Authorization", "Bearer " + token)
                .multiPart("name", formData.getString("name"))

                .multiPart("isDefaultPermission", String.valueOf(formData.getBoolean("isDefaultPermission")))
                .multiPart("users", String.join(",", formData.getJSONArray("users").toList().stream().map(Object::toString).toArray(String[]::new)))
                .multiPart("document", file)
                .when()
                .post();

        // Output and Assert
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 201, "❌ Folder creation failed.");
    }
}
