package wiremock;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.springframework.http.HttpStatus.*;

public class AuthorisationScenarios extends BaseTest {

    private final String scenarioName = "Authorisation test";
    private final String userName = "jeff";
    private final String userPass = "JeffPass";
    private final String responseBody = "Information you are looking for";


    @BeforeMethod
    public void prepareStubs() {
        server.stubFor(get(urlEqualTo(BASE_PATH))
                .inScenario(scenarioName)
                .whenScenarioStateIs(STARTED)
                .willReturn(temporaryRedirect(LOGIN_PATH).withStatus(TEMPORARY_REDIRECT.value()))
                .willSetStateTo("Authorisation_is_needed"));

        server.stubFor(get(urlEqualTo(LOGIN_PATH))
                .withBasicAuth(userName, userPass+"w")
                .inScenario(scenarioName)
                .whenScenarioStateIs("Authorisation_is_needed")
                .willReturn(temporaryRedirect(BASE_PATH).withStatus(TEMPORARY_REDIRECT.value()))
                .willSetStateTo("Authorised"));

        server.stubFor(get(urlEqualTo(BASE_PATH))
                .inScenario(scenarioName)
                .whenScenarioStateIs("Authorised")
                .willReturn(ok()
                        .withHeader("Content-Type", MediaType.TEXT_PLAIN_VALUE)
                        .withBody(responseBody)));
    }

    @Test
    public void testFileDownload() {
        Response response = RestAssured.given()
                .auth()
                .preemptive()
                .basic(userName, userPass + "w")
                .basePath(BASE_PATH)
                .request(Method.GET);


        Assert.assertEquals(response.statusCode(), OK.value());
        Assert.assertEquals(response.header("Content-Type"), MediaType.TEXT_PLAIN_VALUE);
        Assert.assertEquals(response.body().print(), responseBody);

    }

//    @Test
//    public void testPost() {
//        server.stubFor(post(urlEqualTo(BASE_PATH))
//                .inScenario(scenarioName)
//                .whenScenarioStateIs(STARTED)
//                .willReturn(temporaryRedirect(LOGIN_PATH).withStatus(307))
//                .willSetStateTo("Authorisation_is_needed"));
//
//        server.stubFor(post(urlEqualTo(LOGIN_PATH))
//                //.withBasicAuth(userName, userPass+"w")
//                .inScenario(scenarioName)
//                .whenScenarioStateIs("Authorisation_is_needed")
//                .willReturn(ok().withStatus(200))
////                .willReturn(temporaryRedirect(BASE_PATH).withStatus(TEMPORARY_REDIRECT.value()))
//                .willSetStateTo("Authorised"));
////
////        server.stubFor(post(urlEqualTo(BASE_PATH))
////                .inScenario(scenarioName)
////                .whenScenarioStateIs("Authorised")
////                .willReturn(ok()
////                        .withHeader("Content-Type", MediaType.TEXT_PLAIN_VALUE)
////                        .withBody(responseBody)));
//
//        Response response = RestAssured.given()
//               // .auth()
//              //  .preemptive()
//              // .basic(userName, userPass + "w")
//                .basePath(BASE_PATH)
//                .request(Method.POST);
//        Assert.assertEquals(response.statusCode(), 200);
//
//    }
}
