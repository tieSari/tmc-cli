package wad.controller;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.UUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import wad.Application;
import wad.domain.Message;
import wad.repository.MessageRepository;
import wad.service.ChatService;
import wad.service.MessageService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ChatTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageRepository messageRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    @Points("W2E04")
    public void messageServiceKayttaaChatServicea() throws Exception {
        Message msg = new Message();
        msg.setMessage("Hello!");
        msg.setPerson("Jack Bauer");

        int count = chatService.getChatCount();
        messageService.addMessage(msg);

        assertTrue("Verify that MessageService uses an injected ChatService, and that a message is asked only once per request.", count + 1 == chatService.getChatCount());
    }

    @Test
    @Points("W2E04")
    public void molemmatViestitTallennetaanTietokantaan() throws Exception {
        messageRepository.deleteAll();

        Message msg = new Message();
        msg.setMessage("Trolo!");
        msg.setPerson("Jack Bauer");

        messageService.addMessage(msg);

        assertEquals("Verify that MessageService stores both the original message and the message from the ChatService.", 2L, messageRepository.count());
    }

    @Test
    @Points("W2E04")
    public void oikeatViestitTallennetaanTietokantaan() throws Exception {
        messageRepository.deleteAll();

        String content = UUID.randomUUID().toString().substring(0, 6);
        Message msg = new Message();
        msg.setMessage(content);

        messageService.addMessage(msg);

        boolean messageFound = false;
        for (Message message : messageRepository.findAll()) {
            if (message.getMessage()== null) {
                continue;
            }

            if (message.getMessage().equals(content)) {
                messageFound = true;
                break;
            }
        }

        assertTrue("Verify that MessageService stores the original message to the database.", messageFound);

    }
}
