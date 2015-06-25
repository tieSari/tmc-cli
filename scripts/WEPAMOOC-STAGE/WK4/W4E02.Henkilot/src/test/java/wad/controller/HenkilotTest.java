package wad.controller;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.UUID;
import static org.junit.Assert.assertEquals;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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
public class HenkilotTest {

    private static final String PERSONS_URI = "/api/persons";

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    @Points("W4E02.1")
    public void canGetPersons() throws Exception {
        mockMvc.perform(get(PERSONS_URI))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Points("W4E02.1")
    public void canCreatePerson() throws Exception {
        String name = "Person name: " + UUID.randomUUID().toString().substring(0, 6);
        String username = "Username: " + UUID.randomUUID().toString().substring(0, 6);
        String password = "Password: " + UUID.randomUUID().toString().substring(0, 6);

        MvcResult res = mockMvc.perform(post(PERSONS_URI).content("{\"name\":\"" + name + "\",\"username\":\"" + username + "\",\"password\":\"" + password + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertNotNull("The response to a create person call should contain a header called Location.", res.getResponse().getHeader("Location"));
        assertTrue("The response to a create person call should contain a header called Location that has the location (URI) of the newly created person.", res.getResponse().getHeader("Location").contains(PERSONS_URI));
    }

    @Test(expected = Throwable.class)
    @Points("W4E02.1")
    public void noneOfTheFieldsShouldBeEmpty() throws Exception {

        mockMvc.perform(post(PERSONS_URI).content("{\"name\":\"" + UUID.randomUUID().toString().substring(0, 6) + "\",\"username\":\"" + UUID.randomUUID().toString().substring(0, 6) + "\",\"password\":\"\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

        mockMvc.perform(post(PERSONS_URI).content("{\"name\":\"\",\"username\":\"" + UUID.randomUUID().toString().substring(0, 6) + "\",\"password\":\"" + UUID.randomUUID().toString().substring(0, 6) + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

        mockMvc.perform(post(PERSONS_URI).content("{\"name\":\"" + UUID.randomUUID().toString().substring(0, 6) + "\",\"username\":\"\",\"password\":\"" + UUID.randomUUID().toString().substring(0, 6) + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @Points("W4E02.1")
    public void noTwoPersonsWithSameName() throws Exception {
        String name = "Person name: " + UUID.randomUUID().toString().substring(0, 6);
        mockMvc.perform(post(PERSONS_URI).content("{\"name\":\"" + name + "\",\"username\":\"" + UUID.randomUUID() + "\",\"password\":\"" + UUID.randomUUID() + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(post(PERSONS_URI).content("{\"name\":\"" + name + "\",\"username\":\"" + UUID.randomUUID() + "\",\"password\":\"" + UUID.randomUUID() + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @Points("W4E02.1")
    public void noTwoPersonsWithSameUsername() throws Exception {
        String username = "Username: " + UUID.randomUUID().toString().substring(0, 6);
        mockMvc.perform(post(PERSONS_URI).content("{\"name\":\"" + UUID.randomUUID() + "\",\"username\":\"" + username + "\",\"password\":\"" + UUID.randomUUID() + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(post(PERSONS_URI).content("{\"name\":\"" + UUID.randomUUID() + "\",\"username\":\"" + username + "\",\"password\":\"" + UUID.randomUUID() + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @Points("W4E02.1")
    public void canGetCreatedPerson() throws Exception {
        String username = "Username: " + UUID.randomUUID();
        MvcResult res = mockMvc.perform(post(PERSONS_URI).content("{\"name\":\"" + UUID.randomUUID() + "\",\"username\":\"" + username + "\",\"password\":\"" + UUID.randomUUID() + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        String location = res.getResponse().getHeader("Location");
        location = location.substring(location.indexOf(PERSONS_URI));

        res = mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andReturn();

        String content = res.getResponse().getContentAsString();
        assertTrue("When the newly created person is requested, it's name should be in the response.", content.contains(username));
    }

    @Test
    @Points("W4E02.1")
    public void cantDeleteCreatedPerson() throws Exception {
        String username = "Username: " + UUID.randomUUID();
        MvcResult res = mockMvc.perform(post(PERSONS_URI).content("{\"name\":\"" + UUID.randomUUID() + "\",\"username\":\"" + username + "\",\"password\":\"" + UUID.randomUUID() + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        String location = res.getResponse().getHeader("Location");
        location = location.substring(location.indexOf(PERSONS_URI));

        mockMvc.perform(MockMvcRequestBuilders.delete(location))
                .andExpect(status().isMethodNotAllowed());

        res = mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andReturn();

        String content = res.getResponse().getContentAsString();
        assertTrue("When the newly created person is requested, it's name should be in the response even after a delete attempt.", content.contains(username));
    }

    @Test
    @Points("W4E02.2")
    public void authenticationFailsWith401WhenInvalidCredentials() throws Exception {
        String username = "Username: " + UUID.randomUUID();
        mockMvc.perform(post("/authenticate").content("{\"username\":\"" + username + "\",\"password\":\"" + UUID.randomUUID() + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post(PERSONS_URI).content("{\"name\":\"" + username + "\",\"username\":\"" + username + "\",\"password\":\"" + UUID.randomUUID() + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // should still fail as the pw is false
        mockMvc.perform(post("/authenticate").content("{\"username\":\"" + username + "\",\"password\":\"" + UUID.randomUUID() + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Points("W4E02.2")
    public void authenticationSuccessfulWithCorrectCredentials() throws Exception {
        String name = "Name: " + UUID.randomUUID();
        String username = "Username: " + UUID.randomUUID();
        String password = "Password: " + UUID.randomUUID();
        mockMvc.perform(post("/authenticate").content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        // creating the person
        mockMvc.perform(post(PERSONS_URI).content("{\"name\":\"" + name + "\",\"username\":\"" + username + "\",\"password\":\"" + password + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // person is created, auth should be ok
        mockMvc.perform(post("/authenticate").content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Points("W4E02.2")
    public void nameAvailableInResponseOnSuccessfulAuthentication() throws Exception {
        String name = "Name: " + UUID.randomUUID();
        String username = "Username: " + UUID.randomUUID();
        String password = "Password: " + UUID.randomUUID();
        mockMvc.perform(post("/authenticate").content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        // creating the person
        mockMvc.perform(post(PERSONS_URI).content("{\"name\":\"" + name + "\",\"username\":\"" + username + "\",\"password\":\"" + password + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // person is created, auth should be ok
        MvcResult res = mockMvc.perform(post("/authenticate").content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertEquals("The response to an /authenticate-call should return the name if authentication is successful.", name, res.getResponse().getContentAsString());
    }
}
