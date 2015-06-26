package hy.tmc.cli.backend.communication;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.base.Optional;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Exercise;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.zipping.DefaultUnzipDecider;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ExerciseDownloaderTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();
    private ArrayList<Exercise> exercises;
    private ExerciseDownloader exDl;

    /**
     * Creates required stubs and example data for downloader.
     */
    @Before
    public void setup() {
        exDl = new ExerciseDownloader();
        exercises = new ArrayList<>();

        Exercise e1 = new Exercise();
        e1.setZipUrl("http://127.0.0.1:8080/ex1.zip");
        e1.setName("Exercise1");
        exercises.add(e1);

        Exercise e2 = new Exercise();
        e2.setZipUrl("http://127.0.0.1:8080/ex2.zip");
        e2.setName("Exercise2");
        exercises.add(e2);

        stubFor(get(urlEqualTo("/ex1.zip"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Exercise 1</response>")));

        stubFor(get(urlEqualTo("/emptyCourse.json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"api_version\":7,\"course\":{\"id\":21,\"name\":\"k2015-tira\","
                                + "\"details_url\":\"https://tmc.mooc.fi/staging/courses/21.json\""
                                + ",\"unlock_url\":\"https://tmc.mooc.fi/staging/courses/21/unlock"
                                + ".json\",\"reviews_url\":\"https://tmc.mooc.fi/staging/courses"
                                + "/21/reviews"
                                + ".json\",\"comet_url\":\"https://tmc.mooc.fi:8443/"
                                + "comet\",\"spyware_urls\":[\"http://staging.spyware."
                                + "testmycode.net/\"],\"unlockables\":[],\"exercises\":[]}}")));

        wireMockRule.stubFor(get(urlEqualTo("/ex2.zip"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Exercise 2</response>")));

        ClientData.setUserData("pihla", "juuh");
    }

    @After
    public void remove() {
        new File("Exercise1.zip").delete();
        new File("Exercise2.zip").delete();
    }

    @Test
    public void downloadExercisesDoesRequests() {
        exDl.downloadFiles(exercises);
        wireMockRule.verify(getRequestedFor(urlEqualTo("/ex1.zip")));
        wireMockRule.verify(getRequestedFor(urlEqualTo("/ex2.zip")));
    }

    @Test
    public void requestsHaveAuth() {
        exDl.downloadFiles(exercises);

        wireMockRule.verify(getRequestedFor(urlEqualTo("/ex1.zip"))
                .withHeader("Authorization", equalTo("Basic cGlobGE6anV1aA==")));

        wireMockRule.verify(getRequestedFor(urlEqualTo("/ex2.zip"))
                .withHeader("Authorization", equalTo("Basic cGlobGE6anV1aA==")));
    }

    @Test
    public void downloadingGivesOutput() {
        assertTrue(exDl.downloadFiles(exercises).or("").endsWith(" exercises downloaded."));
    }

    @Test
    public void exerciseListIsEmpty() throws IOException, ProtocolException {
        Optional<String> output = exDl.downloadExercises("http://127.0.0.1:8080/emptyCourse.json");
        assertTrue(output.or("").contains("No exercises to download."));
    }

    @Test
    public void downloadedExercisesExists() {
        exDl.downloadFiles(exercises);
        File exercise1 = new File("Exercise1.zip");
        assertTrue("File Exercise1 was not downloaded to the fs", exercise1.exists());
        File exercise2 = new File("Exercise2.zip");
        assertTrue("File Exercise2 was not downloaded to the fs", exercise2.exists());
    }

    @Test
    public void downloadedExercisesHasContent() {
        exDl.downloadFiles(exercises);

        String ex1content;
        try {
            ex1content = new String(Files.readAllBytes(Paths.get("Exercise1.zip")));
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            ex1content = "";
        }
        String ex2content;
        try {
            ex2content = new String(Files.readAllBytes(Paths.get("Exercise2.zip")));
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            ex2content = "";
        }
        assertEquals("<response>Exercise 1</response>", ex1content);
        assertEquals("<response>Exercise 2</response>", ex2content);
    }

    @Test
    public void doesntCallUnzipOnLockedExercise() {
        DefaultUnzipDecider mockedDecider = mock(DefaultUnzipDecider.class);
        exDl = new ExerciseDownloader(mockedDecider);
        exercises.get(0).setLocked(true);
        exercises.get(1).setLocked(true);
        exDl.downloadFiles(exercises);
        

        verify(mockedDecider, times(0)).canBeOverwritten(anyString());
        verify(mockedDecider, times(0)).readTmcprojectYml(any(Path.class));
    }
}
