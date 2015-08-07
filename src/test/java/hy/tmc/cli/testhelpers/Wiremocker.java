package hy.tmc.cli.testhelpers;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import hy.tmc.cli.CliSettings;
import hy.tmc.core.communication.UrlHelper;

public class Wiremocker {

    private UrlHelper helper = new UrlHelper(new CliSettings());

    public WireMockServer wiremockSubmitPaths() {
        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("/user"))
                .withHeader("Authorization", containing("Basic dGVzdDoxMjM0"))
                .willReturn(
                        aResponse()
                        .withStatus(200)
                )
        );
        wiremockGET(wireMockServer, "/courses.json", ExampleJson.allCoursesExample);
        return wireMockServer;
    }

    public WireMockServer mockAnyUserAndSubmitPaths() {
        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("/user"))
                .willReturn(
                        aResponse()
                        .withStatus(200)
                )
        );
        wiremockGET(wireMockServer, "/courses.json", ExampleJson.allCoursesExample);
        return wireMockServer;
    }

    public void wiremockFailingSubmit(WireMockServer server) {
        wiremockGET(server, "/courses/313.json", ExampleJson.failingCourse);
        wiremockPOST(server, "/exercises/285/submissions.json", ExampleJson.failedSubmitResponse);
        wiremockGET(server, "/submissions/7777.json", ExampleJson.failedSubmission);
    }

    public void wireMockSuccesfulSubmit(WireMockServer server) {
        wiremockGET(server, "/courses/3.json", ExampleJson.courseExample);
        wiremockPOST(server, "/exercises/286/submissions.json", ExampleJson.pasteResponse);
        wiremockGET(server, "/submissions/1781.json", ExampleJson.successfulSubmission);
    }

    public void wireMockExpiredSubmit(WireMockServer server) {
        wiremockGET(server, "/courses/21.json", ExampleJson.expiredCourseExample);
        wiremockPOST(server, "/exercises/1393/submissions.json", ExampleJson.pasteResponse);
        wiremockGET(server, "/submissions/1781.json", ExampleJson.successfulSubmission);
    }

    /*
     * When httpGet-request is sent to http://127.0.0.1:8080/ + urlToMock, wiremock returns returnBody
     */
    private void wiremockGET(WireMockServer server, final String urlToMock, final String returnBody) {

        server.stubFor(get(urlEqualTo(helper.withParams(urlToMock)))
                .willReturn(aResponse()
                        .withBody(returnBody)
                )
        );
    }

    /*
     * When httpPost-request is sent to http://127.0.0.1:8080/ + urlToMock, wiremock returns returnBody
     */
    private void wiremockPOST(WireMockServer server, final String urlToMock, final String returnBody) {
        server.stubFor(post(urlEqualTo(helper.withParams(urlToMock)))
                .willReturn(aResponse()
                        .withBody(returnBody)
                )
        );
    }
}