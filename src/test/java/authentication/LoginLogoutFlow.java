import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class LoginLogoutFlow {

    @Test
    public void testLoginAndLogout() {
        // 1. Login & get token
        Response loginResponse = RestAssured
                .given()
                .baseUri("https://apisootra.nyxwolves.com")
                .basePath("/customer/auth/login")
                .contentType(ContentType.JSON)
                .body("{\"email\":\"pandeeswaran617@gmail.com\", \"password\":\"PAndeS@28757\"}")
                .when()
                .post();

        String token = loginResponse.jsonPath().getString("data.accessToken");
        System.out.println("Token: " + token);

        // 2. Logout with token
        RestAssured
                .given()
                .baseUri("https://apisootra.nyxwolves.com")
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/customer/auth/logout")
                .then()
                .log().body()
                .statusCode(200);
    }
}
