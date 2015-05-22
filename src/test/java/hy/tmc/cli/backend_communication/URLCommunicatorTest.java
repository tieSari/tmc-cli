package hy.tmc.cli.backend_communication;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import hy.tmc.cli.backendCommunication.HTTPResult;
import hy.tmc.cli.backendCommunication.URLCommunicator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class URLCommunicatorTest {

    private HttpClient client;
    @Rule
    public WireMockRule wireMockRule = new WireMockRule();
    @Before
    public void createClient() {      
        this.client = URLCommunicator.createClient();
    }
    
    @Test
    public void okWithValidParams() {
        stubFor(get(urlEqualTo("/"))
                .withHeader("Authorization", containing("Basic dGVzdDoxMjM0"))
                .willReturn(
                        aResponse()
                        .withStatus(200)
                )
        );
        HTTPResult result = URLCommunicator.makeGetRequest(client, "http://127.0.0.1:8080", "test:1234");
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
        
        HTTPResult result = URLCommunicator.makeGetRequest(client, "http://127.0.0.1:8080/vaaraurl", "test:1234");
        assertEquals(400, result.getStatusCode());
    }
    
    @Test
    public void notFoundWithoutValidParams() {
        // next will fail, becouse wiremock does not accept headers like that.
        HTTPResult result = URLCommunicator.makeGetRequest(client, "http://127.0.0.1:8080/", "ihanvaaraheaderi:1234");
        assertEquals(404, result.getStatusCode());
    }

    @Test
    public void createGetRequest() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        stubFor(get(urlEqualTo("/"))
                .withHeader("Authorization", containing("Basic dGVzdDoxMjM0"))
                .willReturn(
                        aResponse()
                        .withStatus(200)
                )
        );
        
        Method createMethod = URLCommunicator.class.getDeclaredMethod("createAndExecuteGet", String.class, String[].class, HttpClient.class);
        createMethod.setAccessible(true);
        Object instance = URLCommunicator.class.newInstance();
        HttpResponse result = (HttpResponse) createMethod.invoke(instance, "http://127.0.0.1:8080/", new String[]{
            "test:1234"
        }, client);

        HttpResponse httpResponse = (HttpResponse) result;
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
    }
}
