package wad.controller;

import fi.helsinki.cs.tmc.edutestutils.Points;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wad.Application;
import wad.domain.Pair;
import wad.service.LoveService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class LoveTest {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    @Points("W2E03")
    public void loveServiceLoytyy() {
        LoveService loves = null;
        try {
            loves = webAppContext.getBean(LoveService.class);
        } catch (Throwable t) {
        }

        if (loves == null) {
            fail("Verify that the LoveService-class has the @Service-annotation.");
        }
    }

    @Test
    @Points("W2E03")
    public void kontrolleriKayttaaLoveServicea() throws Exception {
        LoveService loves = null;
        try {
            loves = webAppContext.getBean(LoveService.class);
        } catch (Throwable t) {
        }

        if (loves == null) {
            fail("Verify that the LoveService-class has the @Service-annotation.");
        }
        int count = loves.getMatched();

        mockMvc.perform(post("/loves")
                .param("nameOne", "jack")
                .param("nameTwo", "bauers"));

        assertTrue("Verify that the LoveController uses an injected LoveService, and that the match is counted only once per request.", count + 1 == loves.getMatched());
    }

    @Test
    @Points("W2E03")
    public void postPyyntoUudelleenohjataan() throws Exception {
        LoveService loves = null;
        try {
            loves = webAppContext.getBean(LoveService.class);
        } catch (Throwable t) {
        }

        if (loves == null) {
            fail("Verify that the LoveService-class has the @Service-annotation.");
        }

        mockMvc.perform(post("/loves")
                .param("nameOne", "jack")
                .param("nameTwo", "bauers"))
                .andExpect(status().is3xxRedirection()).andReturn();
    }

    @Test
    @Points("W2E03")
    public void uudelleenOhjauksessaMatch() throws Exception {
        LoveService loves = null;
        try {
            loves = webAppContext.getBean(LoveService.class);
        } catch (Throwable t) {
        }

        if (loves == null) {
            fail("Verify that the LoveService-class has the @Service-annotation.");
        }

        Pair p = new Pair();
        p.setNameOne("jack");
        p.setNameTwo("bauers");

        mockMvc.perform(post("/loves")
                .param("nameOne", "jack")
                .param("nameTwo", "bauers"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("match", loves.countMatch(p)));
    }

    @Test
    @Points("W2E03")
    public void uudelleenOhjauksessaPair() throws Exception {
        LoveService loves = null;
        try {
            loves = webAppContext.getBean(LoveService.class);
        } catch (Throwable t) {
        }

        if (loves == null) {
            fail("Verify that the LoveService-class has the @Service-annotation.");
        }

        Pair p = new Pair();
        p.setNameOne("otto");
        p.setNameTwo("juha");

        mockMvc.perform(post("/loves")
                .param("nameOne", "otto")
                .param("nameTwo", "juha"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("pair", p));
    }

    @Test
    @Points("W2E03")
    public void uudelleenOhjausLovesiin() throws Exception {
        LoveService loves = null;
        try {
            loves = webAppContext.getBean(LoveService.class);
        } catch (Throwable t) {
        }

        if (loves == null) {
            fail("Verify that the LoveService-class has the @Service-annotation.");
        }

        Pair p = new Pair();
        p.setNameOne("matti");
        p.setNameTwo("teppo");

        mockMvc.perform(post("/loves")
                .param("nameOne", "matti")
                .param("nameTwo", "teppo"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("pair", p))
                .andExpect(redirectedUrl("/loves"));

    }
}
