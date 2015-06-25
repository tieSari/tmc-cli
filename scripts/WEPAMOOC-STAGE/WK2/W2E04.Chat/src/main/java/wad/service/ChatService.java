package wad.service;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import wad.domain.Message;

/*
 Thanks Mikael! -- everyone, go and support http://rendezvouspark.net/
 */
@Service
public class ChatService {

    private int chatCount = 0;
    
    private static final String REQUEST_URL_TEMPLATE
            = "http://ikanna-usen.artificial-solutions.com/ikanna-usen/"
            + "?ARTISOLCMD_TEMPLATE=STANDARDJSON&viewtype=STANDARDJSON"
            + "&viewname=STANDARDJSON&command=request&userinput={0}";

    public Message getAnswerToMessage(Message msg) throws IOException {
        String res = new RestTemplate().getForObject(REQUEST_URL_TEMPLATE, String.class, URLEncoder.encode(msg.getMessage(), "UTF-8"));
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(res);
        JsonNode responseDataNode = rootNode.get("responseData");
        if (!responseDataNode.isObject()) {
            throw new IOException("Invalid response from chat bot (responseData)");
        }

        JsonNode answerNode = responseDataNode.get("answer");
        if (!answerNode.isValueNode()) {
            throw new IOException("Invalid response from chat bot (answer)");
        }

        String answer = answerNode.getTextValue();

        Message message = new Message();
        message.setPerson("Anna");
        message.setMessage(URLDecoder.decode(answer, "UTF-8"));
        
        chatCount++;
        
        return message;
    }

    public int getChatCount() {
        return chatCount;
    }
}
