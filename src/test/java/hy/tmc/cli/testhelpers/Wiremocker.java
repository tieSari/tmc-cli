package hy.tmc.cli.testhelpers;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import com.github.tomakehurst.wiremock.WireMockServer;

import fi.helsinki.cs.tmc.core.communication.UrlHelper;

import hy.tmc.cli.CliSettings;

import java.net.URI;
import java.net.URISyntaxException;

public class Wiremocker {

    private UrlHelper helper = new UrlHelper(new CliSettings());

    public WireMockServer wiremockSubmitPaths() throws URISyntaxException {
        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();

        wireMockServer.stubFor(
            get(urlEqualTo("/user")).withHeader("Authorization", containing("Basic dGVzdDoxMjM0"))
                .willReturn(aResponse().withStatus(200)));
        wiremockGET(wireMockServer, new URI("/courses.json"), ExampleJson.allCoursesExample);
        return wireMockServer;
    }

    public WireMockServer mockAnyUserAndSubmitPaths() throws URISyntaxException {
        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("/user")).willReturn(aResponse().withStatus(200)));
        wiremockGET(wireMockServer, new URI("/courses.json"), ExampleJson.allCoursesExample);
        return wireMockServer;
    }

    public void wiremockFailingSubmit(WireMockServer server) throws URISyntaxException {
        wiremockGET(server, new URI("/courses/313.json"), ExampleJson.failingCourse);
        wiremockPOST(server, new URI("/exercises/285/submissions.json"), ExampleJson.failedSubmitResponse);
        wiremockGET(server, new URI("/submissions/7777.json"), ExampleJson.failedSubmission);
    }

    public void wireMockSuccesfulSubmit(WireMockServer server) throws URISyntaxException {
        wiremockGET(server, new URI("/courses/3.json"), ExampleJson.courseExample);
        wiremockPOST(server, new URI("/exercises/286/submissions.json"), ExampleJson.pasteResponse);
        wiremockGET(server, new URI("/submissions/1781.json"), ExampleJson.successfulSubmission);
    }

    public void wireMockExpiredSubmit(WireMockServer server) throws URISyntaxException {
        wiremockGET(server, new URI("/courses/21.json"), ExampleJson.expiredCourseExample);
        wiremockPOST(server, new URI("/exercises/1393/submissions.json"), ExampleJson.pasteResponse);
        wiremockGET(server, new URI("/submissions/1781.json"), ExampleJson.successfulSubmission);
    }

    /*
     * When httpGet-request is sent to http://127.0.0.1:8080/ + urlToMock, wiremock returns returnBody
     */
    private void wiremockGET(WireMockServer server, final URI urlToMock,
        final String returnBody) {

        try {
            server.stubFor(get(urlEqualTo(helper.withParams(urlToMock).toString()))
                    .willReturn(aResponse().withBody(returnBody)));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /*
     * When httpPost-request is sent to http://127.0.0.1:8080/ + urlToMock, wiremock returns returnBody
     */
    private void wiremockPOST(WireMockServer server, final URI urlToMock,
        final String returnBody) {
        try {
            server.stubFor(post(urlEqualTo(helper.withParams(urlToMock).toString()))
                    .willReturn(aResponse().withBody(returnBody)));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
