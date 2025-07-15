package usermanagement;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class fetchuserprofile {

    @Test
    public void testGetCustomerProfile() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("Data.json");
        assert is != null;
        JSONObject jsonData = new JSONObject(new JSONTokener(is));

        String baseUri = jsonData.getString("baseUri");
        String token = jsonData.getString("token");

        JSONObject profileData = jsonData.getJSONObject("getCustomerProfile");
        String basePath = profileData.getString("basePath");
        int expectedStatus = profileData.getInt("expectedStatusCode");

        Response response = given()
                .baseUri(baseUri)
                .basePath(basePath)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .get();

        response.prettyPrint();
        assertEquals(response.getStatusCode(), expectedStatus);
    }
}
