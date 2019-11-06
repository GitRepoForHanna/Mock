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

public class ServerError extends BaseTest{

    @BeforeMethod
    public void prepareStubs() {
        server.stubFor(post(urlEqualTo(BASE_PATH + ERROR_PATH))
                .willReturn(serverError()));
    }

    @Test
    public void testServerError() {
        Response response = RestAssured.given()
                .basePath(BASE_PATH + ERROR_PATH)
                .request(Method.POST);
        Assert.assertEquals(response.statusCode(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
