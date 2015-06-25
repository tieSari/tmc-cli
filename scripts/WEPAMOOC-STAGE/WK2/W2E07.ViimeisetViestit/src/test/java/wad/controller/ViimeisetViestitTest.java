package wad.controller;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import wad.Application;
import wad.domain.Message;
import wad.repository.MessageRepository;
import wad.service.MessageService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ViimeisetViestitTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Test
    @Points("W2E07")
    public void palauttaaKymmenenViestia() throws Exception {
        messageRepository.deleteAll();
        
        for (int i = 0; i < 20; i++) {
            Message msg = new Message();
            String text = UUID.randomUUID().toString().substring(0, 6);
            msg.setMessage(text);

            messageService.addMessage(msg);
        }

        assertEquals("Varmista että messageService palauttaa kymmenen viestiä.", 10, messageService.list().size());
    }

    @Test
    @Points("W2E07")
    public void palauttaaViimeisetKymmenenViestia() throws Exception {
        messageRepository.deleteAll();
        
        Set<String> viestit = new HashSet<>();
        for (int i = 20; i > 0; i--) {
            Message msg = new Message();
            String text = UUID.randomUUID().toString().substring(0, 6);
            msg.setMessage(text);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_YEAR, i + 1);

            msg.setMessageDate(cal.getTime());
            
            if (i > 10) {
                viestit.add(text);
            }

            messageService.addMessage(msg);
        }
        
        List<Message> messages = messageService.list();
        
        assertEquals("Varmista että messageService palauttaa kymmenen viestiä.", 10, messages.size());

        for (Message message : messages) {
            viestit.remove(message.getMessage());
        }

        assertTrue("Varmista että messageService palauttaa viimeisimmät kymmenen viestiä.", viestit.isEmpty());
    }

}
