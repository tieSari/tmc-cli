package hy.tmc.cli.backendcommunication;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.HttpResponse;

import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;





public class UrlCommunicatorTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();
    
    @Test
    public void okWithValidParams() {
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
    public void badRequestWithoutValidUrl() {
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
        // next will fail, becouse wiremock does not accept headers like that.
        HttpResult result = UrlCommunicator.makeGetRequest("http://127.0.0.1:8080/", "ihanvaaraheaderi:1234");
        assertEquals(403, result.getStatusCode());
    }

    @Test
    public void createGetRequest() throws ClassNotFoundException,
            NoSuchMethodException, InstantiationException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        stubFor(get(urlEqualTo("/"))
                .withHeader("Authorization", containing("Basic dGVzdDoxMjM0"))
                .willReturn(
                        aResponse()
                        .withStatus(200)
                )
        );
        
        Method createMethod = UrlCommunicator.class.getDeclaredMethod("createAndExecuteGet",
                String.class,
                String[].class);
        createMethod.setAccessible(true);
        Object instance = UrlCommunicator.class.newInstance();
        HttpResponse result = (HttpResponse) createMethod.invoke(instance, "http://127.0.0.1:8080/", new String[]{
            "test:1234"
        });

        HttpResponse httpResponse = (HttpResponse) result;
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
    }
}
