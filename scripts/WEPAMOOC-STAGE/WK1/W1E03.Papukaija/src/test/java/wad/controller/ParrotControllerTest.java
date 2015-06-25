package wad.controller;

import fi.helsinki.cs.tmc.edutestutils.Points;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.web.context.WebApplicationContext;
import wad.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Points("W1E03")
public class ParrotControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void kraaWithNoMessage() throws Exception {
        mockMvc.perform(get("/bob"))
                .andExpect(status().isOk())
                .andExpect(content().string("Krraaa"));
    }

    @Test
    public void kraaLol() throws Exception {
        mockMvc.perform(get("/bob").param("message", "lol"))
                .andExpect(status().isOk())
                .andExpect(content().string("Krraaa lol"));
    }
    
    @Test
    public void testPapukaijaLol() throws Exception {
        mockMvc.perform(get("/bob").param("message", "lol"))
                .andExpect(status().isOk())
                .andExpect(content().string("Krraaa lol"));
    }
}
