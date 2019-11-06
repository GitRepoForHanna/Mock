package wiremock;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class FileDownloading extends BaseTest{

    @BeforeMethod
    public void prepareStubs() {
        server.stubFor(get(urlEqualTo(BASE_PATH))
                .willReturn(aResponse().withBodyFile("weather.txt")));
    }

    @Test
    public void testFileDownload() {
        Response response = RestAssured.given()
                .basePath(BASE_PATH)
                .request(Method.GET);
        Assert.assertEquals(response.statusCode(), HttpStatus.OK.value());
    }
}
