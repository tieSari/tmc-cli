package hy.tmc.cli.backendcommunication;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import hy.tmc.cli.configuration.ClientData;
import java.io.File;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import org.junit.Rule;
import org.junit.Test;

public class UrlCommunicatorTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Test
    public void okWithValidParams() {
        new UrlCommunicator();
        stubFor(get(urlEqualTo("/"))
                .withHeader("Authorization", containing("Basic dGVzdDoxMjM0"))
                .willReturn(
                        aResponse()
                        .withStatus(200)
                )
        );
        HttpResult result = UrlCommunicator.makeGetRequest("http://127.0.0.1:8080", "test:1234");
        assertEquals(200, result.getStatusCode());
    }

    @Test
    public void badRequestWithoutValidURL() {
        stubFor(get(urlEqualTo("/vaaraurl"))
                .withHeader("Authorization", containing("Basic dGVzdDoxMjM0"))
                .willReturn(
                        aResponse()
                        .withStatus(400)
                )
        );

        HttpResult result = UrlCommunicator.makeGetRequest("http://127.0.0.1:8080/vaaraurl", "test:1234");
        assertEquals(400, result.getStatusCode());
    }

    @Test
    public void notFoundWithoutValidParams() {
        HttpResult result = UrlCommunicator.makeGetRequest("http://127.0.0.1:8080/", "ihanvaaraheaderi:1234");
        assertEquals(403, result.getStatusCode());
    }

    @Test
    public void httpPostAddsFileToRequest() throws IOException {
        ClientData.setUserData("test", "1234");
        stubFor(post(urlEqualTo("/kivaurl"))
                .withHeader("Authorization", containing("Basic dGVzdDoxMjM0"))
                .withRequestBody(containing("submission[file]"))
                .withRequestBody(containing("test.zip"))
                .willReturn(
                        aResponse()
                        .withBody("All tests passed")
                        .withStatus(200)
                )
        );
        File testFile = new File("testResources/test.zip");
        HttpResult result = UrlCommunicator.makePostWithFile(testFile, "http://127.0.0.1:8080/kivaurl");
        ClientData.clearUserData();
        assertEquals("All tests passed", result.getData());
    }

    @Test
    public void badGetRequestIsCatched() {
        HttpResult makeGetRequest = UrlCommunicator.makeGetRequest("asasdasd", "chang:/\\\\eiparas");
        assertEquals(UrlCommunicator.BAD_REQUEST, makeGetRequest.getStatusCode());
    }

}
