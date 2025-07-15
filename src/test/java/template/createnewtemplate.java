package template;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.File;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class createnewtemplate {


    @Test
    public void testCreateTemplate() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonData = new JSONObject(new JSONTokener(is));
        JSONObject templateData = jsonData.getJSONObject("createTemplate");

        String token = jsonData.getString("token"); // Optional: or call getAccessToken()
        String baseUri = jsonData.getString("baseUri");

        File fileUpload = new File(templateData.getString("fileUploadPath"));
        File recDoc = new File(templateData.getString("recDocPath"));

        Response response = given()
                .baseUri(baseUri)
                .basePath(templateData.getString("endpoint"))
                .header("Authorization", "Bearer " + token)
                .multiPart("name", templateData.getString("templateName"))
                .multiPart("description", templateData.getString("description"))
                .multiPart("type", templateData.getString("type"))
                .multiPart("source", templateData.getString("source"))
                .multiPart("deadLine", templateData.getString("deadline"))
                .multiPart("status", templateData.getString("status"))
                .multiPart("content", templateData.getString("content"))
                .multiPart("mac_res", templateData.getString("macRes"))
                .multiPart("win_res", templateData.getString("winRes"))
                .multiPart("importTemplateId", "")
                .multiPart("clients", templateData.getString("clientId"))
                .multiPart("questions", templateData.getJSONArray("questions").toString())
                .multiPart("configs", templateData.getJSONObject("configs").toString())
                .multiPart("fileUpload", fileUpload)
                .multiPart("rec_doc", recDoc)
                .when()
                .post();

        response.prettyPrint();
        assertEquals(response.getStatusCode(), templateData.getInt("expectedStatusCode"), "❌ Template creation failed.");
    }
}
