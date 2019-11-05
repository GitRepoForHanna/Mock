package wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import org.testng.annotations.*;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class BaseTest {

    public static final String BASE_PATH = "/some/thing";
    public static final String ERROR_PATH = "/error";
    public static final String LOGIN_PATH = "/perform_login";
    protected WireMockServer server;
    protected static final String HOST = System.getProperty("end.point");
    private static String rootDir = "src/test/resources/filesDir";

    @BeforeClass
    public void startServer() {
        server = new WireMockServer(options()
                .dynamicPort()
                .bindAddress(HOST)
                .usingFilesUnderDirectory(rootDir)
                .enableBrowserProxying(true));
        server.start();
        RestAssured.baseURI = buildURI(server.port());
    }

    public String buildURI(int port) {
        return String.format("http://%s:%d",HOST, port);
    }

    @AfterClass
    public void endServer() {
        server.stop();
    }
}
