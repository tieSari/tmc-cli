package hy.tmc.cli.backend_communication;

import hy.tmc.cli.backendCommunication.HTTPResult;
import hy.tmc.cli.backendCommunication.URLCommunicator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.http.HttpResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class URLCommunicatorTest {

    @Test
    public void createGetMethodWorksWithValidParams() {
        HTTPResult result = URLCommunicator.makeGetRequest("https://tmc.mooc.fi/hy", "test:1234");
        assertEquals(result.getStatusCode(), 200);
    }

    @Test
    public void createGetMethodDoNotWorkWithoutValidParams() {
        HTTPResult result = URLCommunicator.makeGetRequest("https://tmc.mooc.fo", "test:1234");
        assertEquals(result.getStatusCode(), 400);
    }

    @Test
    public void createGetRequest() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Method[] declaredMethods = URLCommunicator.class.getDeclaredMethods();
        Method createMethod = URLCommunicator.class.getDeclaredMethod("createAndExecuteGet", String.class, String[].class);
        createMethod.setAccessible(true);
        Object instance = URLCommunicator.class.newInstance();
        HttpResponse result = (HttpResponse) createMethod.invoke(instance, "https://tmc.mooc.fi/hy", new String[]{
            "test"
        });

        HttpResponse httpResponse = (HttpResponse) result;
        System.out.println("Statusline: " + httpResponse.getStatusLine());
        assertEquals(httpResponse.getStatusLine(), "HTTP/1.1 200 OK");
    }

}
