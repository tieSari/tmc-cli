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
@Points("W1E05")
public class FormControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void nahdaanEdellisessaPyynnossaLahetettyViesti() throws Exception {
        mockMvc.perform(get("/submit").param("data", "viesti"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("data"))
                .andExpect(model().attribute("data", "nothing.."))
                .andExpect(forwardedUrl("/WEB-INF/views/page.jsp"));

        mockMvc.perform(get("/submit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("data"))
                .andExpect(model().attribute("data", "viesti"))
                .andExpect(forwardedUrl("/WEB-INF/views/page.jsp"));

        mockMvc.perform(get("/submit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("data"))
                .andExpect(model().attribute("data", "viesti"))
                .andExpect(forwardedUrl("/WEB-INF/views/page.jsp"));
    }
}
