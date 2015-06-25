package wad.controller;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Map;
import java.util.UUID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import wad.Application;
import wad.domain.Message;
import wad.repository.MessageRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Points("W3E05")
public class ViestiApiTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private WebApplicationContext webAppContext;
    @Autowired
    private ListableBeanFactory listableBeanFactory;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void hasNoControllers() {
        Map<String, Object> beans = listableBeanFactory.getBeansWithAnnotation(Controller.class);

        for (Object bean : beans.values()) {
            if (bean.getClass().getName().contains("MessageController")) {
                continue;
            }

            assertFalse("You should not have any controllers outside MessageController in your application. You have " + bean.getClass().getName(), bean.getClass().getPackage().toString().contains("wad."));
        }
    }

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get("/api/messages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON));
    }

    @Test
    public void testGetSingleNotFound() throws Exception {
        mockMvc.perform(get("/api/messages/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPost() throws Exception {
        String message = UUID.randomUUID().toString().substring(0, 6);
        String person = UUID.randomUUID().toString().substring(0, 6);

        mockMvc.perform(post("/api/messages").content("{\"message\":\"" + message + "\",\"person\":\"" + person + "\"}"))
                .andExpect(status().is2xxSuccessful());

        boolean found = false;
        for (Message msg : messageRepository.findAll()) {
            if (person.equals(msg.getPerson()) && message.equals(msg.getMessage())) {
                found = true;
                break;
            }
        }

        assertTrue("When you post a message through the message API, it should be available in the database.", found);

    }
}
