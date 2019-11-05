package wiremock;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class PostMethodScenario extends BaseTest{

    @BeforeMethod
    public void prepareStubs() {
        server.stubFor(post(urlEqualTo(BASE_PATH))
                .willReturn(ok()));

//        RestAssured.baseURI = buildURI(server.port());
    }

    @Test
    public void testPost() {
        Response response = RestAssured.given()
                .basePath(BASE_PATH)
                .request(Method.POST);
        Assert.assertEquals(response.statusCode(), HttpStatus.OK.value());
    }

}
