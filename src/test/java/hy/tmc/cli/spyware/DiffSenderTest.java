package hy.tmc.cli.spyware;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import static org.junit.Assert.assertEquals;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.io.Files;


import hy.tmc.cli.backend.communication.HttpResult;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.domain.Course;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class DiffSenderTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();
    
    @Rule
    public ExpectedException exceptedEx = ExpectedException.none();

    private final String spywareUrl = "http://127.0.0.1:8080/spyware";
    private DiffSender sender;
    private String originalServerUrl;
    private ConfigHandler config;

    /**
     * Logins the users and creates fake server.
     */
    @Before
    public void setup() throws IOException {
        config = new ConfigHandler();
        config.writeServerAddress("http://127.0.0.1:8080");
        ClientData.setUserData("test", "1234");
        sender = new DiffSender();
        startWiremock();
    }

    @Test
    public void testSendToSpywareWithFile() throws IOException {
        final File file = new File("testResources/test.zip");
        DiffSender sender = new DiffSender();
        HttpResult res = sender.sendToUrl(file,
                spywareUrl);
        assertEquals(200, res.getStatusCode());
    }

    @Test
    public void testSendToAllUrlsWithFile() throws IOException {
        final File file = new File("testResources/test.zip");
        Course testCourse = new Course();
        List<String> urls = new ArrayList<>();
        urls.add(spywareUrl);
        testCourse.setSpywareUrls(
                urls
        );
        List<HttpResult> results = sender.sendToSpyware(file, testCourse);
        for (HttpResult res : results) {
            assertEquals(200, res.getStatusCode());
        }
    }

    @Test
    public void testSendToSpywareWithByteArray() throws IOException {
        final File file = new File("testResources/test.zip");
        byte[] byteArray = Files.toByteArray(file);
        DiffSender sender = new DiffSender();
        HttpResult res = sender.sendToUrl(byteArray,
                spywareUrl);
        assertEquals(200, res.getStatusCode());
    }
    
    @Test
    public void requestWithInvalidParams() throws IOException {
        final File file = new File("testResources/test.zip");
        byte[] byteArray = Files.toByteArray(file);
        DiffSender sender = new DiffSender();
        HttpResult res = sender.sendToUrl(byteArray,
                "vaaraUrl");
        assertNull(res);
    }
    
    @Test
    public void testSendToAllUrlsWithByteArray() throws IOException {
        final File file = new File("testResources/test.zip");
        byte[] byteArray = Files.toByteArray(file);
        Course testCourse = new Course();
        testCourse.setSpywareUrls(
                Arrays.asList(new String[]{spywareUrl})
        );
        List<HttpResult> results = sender.sendToSpyware(byteArray, testCourse);
        for (HttpResult res : results) {
            assertEquals(200, res.getStatusCode());
        }
    }
    
    @Test
    public void spywarePostIncludesFileAndHeaders() throws IOException {
        File testFile = new File("testResources/test.zip");
        HttpResult res = sender.sendToUrl(testFile, spywareUrl);
        assertEquals(200, res.getStatusCode());
    }

    private void startWiremock() {
        stubFor(post(urlEqualTo("/spyware"))
                .withHeader("X-Tmc-Version", equalTo("1"))
                .withHeader("X-Tmc-Username", equalTo(ClientData.getUsername()))
                .withHeader("X-Tmc-Password", equalTo(ClientData.getPassword()))
                .willReturn(aResponse().withBody("OK").withStatus(200)));
    }

    /**
     * Clears the state of test environment.
     */
    @After
    public void cleanUp() throws IOException {
        ClientData.clearUserData();
        config.writeServerAddress("https://tmc.mooc.fi/staging");
    }
}
