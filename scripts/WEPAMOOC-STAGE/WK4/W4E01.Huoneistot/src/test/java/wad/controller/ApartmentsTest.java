package wad.controller;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.UUID;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
@Points("W4E01")
public class ApartmentsTest {

    private static final String APARTMENTS_URI = "/api/apartments";
    
    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void canGetApartments() throws Exception {
        mockMvc.perform(get(APARTMENTS_URI))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void canCreateApartment() throws Exception {
        String name = "Apartment name: " + UUID.randomUUID().toString().substring(0, 6);

        MvcResult res = mockMvc.perform(post(APARTMENTS_URI).content("{\"name\":\"" + name + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertNotNull("The response to a create apartment call should contain a header called Location.", res.getResponse().getHeader("Location"));
        assertTrue("The response to a create apartment call should contain a header called Location that has the location (URI) of the newly created apartment.", res.getResponse().getHeader("Location").contains(APARTMENTS_URI));
    }

    @Test(expected = Throwable.class)
    public void apartmentNameShouldNotBeEmpty() throws Exception {
        mockMvc.perform(post(APARTMENTS_URI).content("{\"name\":\"\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void cannotCreateTwoApartmentsWithSameName() throws Exception {
        String name = "Apartment name: " + UUID.randomUUID().toString().substring(0, 6);
        mockMvc.perform(post(APARTMENTS_URI).content("{\"name\":\"" + name + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(post(APARTMENTS_URI).content("{\"name\":\"" + name + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void canGetCreatedApartment() throws Exception {
        String name = "Apartment name: " + UUID.randomUUID().toString().substring(0, 6);
        MvcResult res = mockMvc.perform(post(APARTMENTS_URI).content("{\"name\":\"" + name + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        String location = res.getResponse().getHeader("Location");
        location = location.substring(location.indexOf(APARTMENTS_URI));

        res = mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andReturn();

        String content = res.getResponse().getContentAsString();
        assertTrue("When the newly created apartment is requested, it's name should be in the response.", content.contains(name));
    }
}
