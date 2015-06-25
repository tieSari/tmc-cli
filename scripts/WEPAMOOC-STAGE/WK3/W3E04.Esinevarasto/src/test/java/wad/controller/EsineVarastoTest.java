package wad.controller;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Map;
import java.util.UUID;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Points("W3E04")
public class EsineVarastoTest {

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
            assertFalse("You should not have any controllers in your application.", bean.getClass().getPackage().toString().contains("wad."));
        }
    }

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"));
    }

    @Test
    public void testGetSingleNotFound() throws Exception {
        mockMvc.perform(get("/items/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPost() throws Exception {
        String name = UUID.randomUUID().toString().substring(0, 6);
        mockMvc.perform(post("/items").content("{\"name\":\"" + name + "\"}"))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"));
    }

}
