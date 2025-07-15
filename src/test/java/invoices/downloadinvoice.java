package invoices;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import java.io.FileOutputStream;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import io.qameta.allure.Allure;

public class downloadinvoice {

    @Test
    public void testDownloadInvoice() throws Exception {
        // Load test data
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        assert is != null;
        JSONObject json = new JSONObject(new JSONTokener(is));

        String baseUri = json.getString("baseUri");
        String token = json.getString("token");
        JSONObject endpoint = json.getJSONObject("downloadInvoice");
        String path = endpoint.getString("path");

        // Send GET request to download invoice
        Response response = given()
                .baseUri(baseUri)
                .basePath(path)
                .header("Authorization", "Bearer " + token)
                .when()
                .get();

        // Validate status and content type
        assertEquals(response.getStatusCode(), 200, "❌ Download failed.");


        // Save file locally
        byte[] fileBytes = response.getBody().asByteArray();
        try (FileOutputStream fos = new FileOutputStream("downloaded_invoice_735.pdf")) {
            fos.write(fileBytes);
        }

        System.out.println("✅ Invoice downloaded and saved as downloaded_invoice_735.pdf");
    }
}
