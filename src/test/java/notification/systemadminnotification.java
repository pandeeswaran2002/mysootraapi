package notification;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import io.qameta.allure.Allure;

public class systemadminnotification {



        @Test
        public void testGetSystemAdminNotifications() {
            InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
            assert is != null;
            JSONObject json = new JSONObject(new JSONTokener(is));

            String baseUri = json.getString("baseUri");
            String defaultToken = json.getString("token");
            String adminToken = json.getString("adminToken");

            JSONObject endpoint = json.getJSONObject("getSystemAdminNotifications");
            String path = endpoint.getString("path");
            JSONObject query = endpoint.getJSONObject("query");

            // Use admin token for system admin APIs
            String tokenToUse = path.startsWith("/api/system-admin") ? adminToken : defaultToken;

            Response response = given()
                    .baseUri(baseUri)
                    .basePath(path)
                    .header("Authorization", "Bearer " + tokenToUse)
                    .contentType(ContentType.JSON)
                    .queryParams(query.toMap())
                    .when()
                    .get();

            response.prettyPrint();
            assertEquals(response.statusCode(), 200, "Expected 200 OK for system admin notification fetch.");
        }
    }

