package wad.controller;

import wad.domain.Guest;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.web.context.WebApplicationContext;
import wad.Application;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class GuestControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    @Points("W1E06.1")
    public void lisaysToimii() throws Exception {
        MvcResult res = mockMvc.perform(get("/guests"))
                .andExpect(model().attributeExists("guests"))
                .andReturn();

        Collection<Guest> guests = (Collection<Guest>) res.getModelAndView().getModel().get("guests");
        for (Guest guest : guests) {
            if (guest.getName() != null && guest.getName().contains("jack bauer")) {
                fail("Kun osoite /guests haetaan ensimmäistä kertaa, siellä ei pitäisi olla \"jack bauer\"-nimistä vierasta.");
            }
        }

        mockMvc.perform(post("/guests").param("name", "jack bauer").param("menu", "liha"));

        res = mockMvc.perform(get("/guests"))
                .andExpect(model().attributeExists("guests"))
                .andReturn();
        guests = (Collection<Guest>) res.getModelAndView().getModel().get("guests");

        boolean jackFound = false;
        for (Guest vieras : guests) {
            if (vieras.getName() != null && vieras.getName().contains("jack bauer")) {
                jackFound = true;
                break;
            }
        }

        if (!jackFound) {
            fail("Kun vieras \"jack bauer\" on lisätty lomakkeella, tulee hänen olla seuraavalla kerralla sivulla näkyvissä.");
        }
    }

    @Test
    @Points("W1E06.1")
    public void nimentontaHenkiloaEiSaaLisata() throws Exception {
        mockMvc.perform(post("/guests").param("name", "").param("menu", "liha"));

        MvcResult res = mockMvc.perform(get("/guests"))
                .andExpect(model().attributeExists("guests"))
                .andReturn();
        Collection<Guest> guests = (Collection<Guest>) res.getModelAndView().getModel().get("guests");

        boolean emptyFound = false;
        for (Guest vieras : guests) {
            if (vieras.getName() == null || vieras.getName().trim().isEmpty()) {
                emptyFound = true;
                break;
            }
        }

        if (emptyFound) {
            fail("Vieraan, jonka nimi on tyhjä, lisäämisen ei pitäisi onnistua.");
        }
    }

    @Test
    @Points("W1E06.1")
    public void nimentontaHenkiloaEiSaaLisata2() throws Exception {
        mockMvc.perform(post("/guests").param("menu", "liha"));

        MvcResult res = mockMvc.perform(get("/guests"))
                .andExpect(model().attributeExists("guests"))
                .andReturn();
        Collection<Guest> guests = (Collection<Guest>) res.getModelAndView().getModel().get("guests");

        boolean emptyFound = false;
        for (Guest vieras : guests) {
            if (vieras.getName() == null || vieras.getName().trim().isEmpty()) {
                emptyFound = true;
                break;
            }
        }

        if (emptyFound) {
            fail("Vieraan, jonka nimi on tyhjä, lisäämisen ei pitäisi onnistua.");
        }
    }

    @Test
    @Points("W1E06.1")
    public void lisayksessaUudellenohjaus() throws Exception {
        mockMvc.perform(post("/guests").param("name", "jurassic park").param("menu", "scientists"))
                .andExpect(redirectedUrl("/guests"));
    }

    @Test
    @Points("W1E06.2")
    public void poistoToimii() throws Exception {
        mockMvc.perform(post("/guests").param("name", "gandhi").param("menu", "kala"));

        MvcResult res = mockMvc.perform(get("/guests"))
                .andExpect(model().attributeExists("guests"))
                .andReturn();
        Collection<Guest> guests = (Collection<Guest>) res.getModelAndView().getModel().get("guests");

        if (guests.isEmpty()) {
            fail("Toteuta vieraiden lisäys ensin.");
        }

        for (Guest guest : new ArrayList<>(guests)) {
            mockMvc.perform(post("/guests/" + guest.getId() + "/delete"));
        }

        res = mockMvc.perform(get("/guests"))
                .andExpect(model().attributeExists("guests"))
                .andReturn();
        guests = (Collection<Guest>) res.getModelAndView().getModel().get("guests");

        if (!guests.isEmpty()) {
            fail("Vieraiden poistaminen ei toimi.");
        }
    }

    @Test
    @Points("W1E06.2")
    public void poistossaUudelleenohjaus() throws Exception {
        mockMvc.perform(post("/guests").param("name", "jed 1 knight").param("menu", "midichlorians"));

        MvcResult res = mockMvc.perform(get("/guests"))
                .andExpect(model().attributeExists("guests"))
                .andReturn();
        Collection<Guest> guests = (Collection<Guest>) res.getModelAndView().getModel().get("guests");

        if (guests.isEmpty()) {
            fail("Toteuta vieraiden lisäys ensin.");
        }

        for (Guest guest : new ArrayList<>(guests)) {
            mockMvc.perform(post("/guests/" + guest.getId() + "/delete"))
                    .andExpect(redirectedUrl("/guests"));
        }
    }

    @Test
    @Points("W1E06.2")
    public void poistoGetillaKielletty() throws Exception {
        mockMvc.perform(post("/guests").param("name", "gandhi").param("menu", "kala"));

        MvcResult res = mockMvc.perform(get("/guests"))
                .andExpect(model().attributeExists("guests"))
                .andReturn();
        Collection<Guest> guests = (Collection<Guest>) res.getModelAndView().getModel().get("guests");

        if (guests.isEmpty()) {
            fail("Toteuta vieraiden lisäys ensin.");
        }

        for (Guest guest : new ArrayList<>(guests)) {
            mockMvc.perform(get("/guests/" + guest.getId() + "/delete"));
        }

        res = mockMvc.perform(get("/guests"))
                .andExpect(model().attributeExists("guests"))
                .andReturn();
        guests = (Collection<Guest>) res.getModelAndView().getModel().get("guests");

        if (guests.isEmpty()) {
            fail("Älä salli vieraiden poistamista GET-tyyppisillä pyynnöillä.");
        }
    }
}
