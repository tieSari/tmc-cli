package wad.controller;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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

import static org.junit.Assert.*;

import wad.Application;
import wad.domain.Task;

import wad.repository.TaskRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class TaskControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private TaskRepository taskRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    @Points("W1E07.1")
    public void listaus() throws Exception {

        MvcResult res = mockMvc.perform(get("/tasks"))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(forwardedUrl("/WEB-INF/views/page.jsp"))
                .andReturn();

        Collection<Task> tasks = (Collection<Task>) res.getModelAndView().getModel().get("tasks");
        for (Task task : tasks) {
            if (task.getName() != null && task.getName().contains("tee testit")) {
                fail("Kun sivu avataan ensimmäistä kertaa, siellä ei pitäisi olla testien tekemisestä muistuttavaa tehtävää.");
            }
        }

        Task t = new Task();
        t.setName("tee testit tehtävälle");
        t.setDone(Boolean.TRUE);
        taskRepository.save(t);

        res = mockMvc.perform(get("/tasks"))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(forwardedUrl("/WEB-INF/views/page.jsp"))
                .andReturn();

        tasks = (Collection<Task>) res.getModelAndView().getModel().get("tasks");

        boolean found = false;
        for (Task task : tasks) {
            if (task.getName() != null && task.getName().contains("tee testit")) {
                found = true;
                break;
            }
        }

        if (!found) {
            fail("Kun tietokantaan on lisätty tehtävä \"tee testit tehtävälle\", tulee sen olla myös model-olion \"tasks\"-nimisessä listassa.");
        }
    }

    @Test
    @Points("W1E07.2")
    public void lisays() throws Exception {
        for (Task task : taskRepository.findAll()) {
            taskRepository.delete(task);
        }

        mockMvc.perform(post("/tasks").param("name", "imuroi").param("done", "false"))
                .andExpect(redirectedUrl("/tasks"));

        assertEquals("Kun lomakkeella on lisätty yksi tehtävä, tulee se näkyä myös tietokannassa.", 1, taskRepository.count());
        assertEquals("imuroi", taskRepository.findAll().get(0).getName());
        assertEquals(Boolean.FALSE, taskRepository.findAll().get(0).getDone());
    }

    @Test
    @Points("W1E07.2")
    public void useammanLisays() throws Exception {
        for (Task task : taskRepository.findAll()) {
            taskRepository.delete(task);
        }

        Set<String> added = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            String name = UUID.randomUUID().toString().substring(0, 6);
            added.add(name);
            mockMvc.perform(post("/tasks").param("name", name).param("done", "true"))
                    .andExpect(redirectedUrl("/tasks"));
        }

        assertEquals("Kun lomakkeella lisätään viisi tehtävää, tulee niiden näkyä myös tietokannassa.", 5, taskRepository.count());

        for (Task task : taskRepository.findAll()) {
            added.remove(task.getName());
            assertEquals("Tehtävien tulee voida olla myös tehtyjä niitä lisättäessä.", Boolean.TRUE, task.getDone());
        }

        assertEquals("Käytäthän tehtävien nimenä aina parametrina \"name\" annettua nimeä?", 0, added.size());
    }

    @Test
    @Points("W1E07.3")
    public void poistoToimii() throws Exception {
        mockMvc.perform(post("/tasks").param("name", "sleep").param("done", "false"));

        MvcResult res = mockMvc.perform(get("/tasks"))
                .andExpect(model().attributeExists("tasks"))
                .andReturn();
        Collection<Task> tasks = (Collection<Task>) res.getModelAndView().getModel().get("tasks");

        if (tasks.isEmpty()) {
            fail("Toteuta tehtävien lisäys ensin.");
        }

        for (Task task : new ArrayList<>(tasks)) {
            mockMvc.perform(post("/tasks/" + task.getId() + "/delete"));
        }

        assertEquals("Tehtävien poistaminen ei toimi.", 0, taskRepository.count());
    }

    @Test
    @Points("W1E07.3")
    public void poistossaUudelleenohjaus() throws Exception {
        mockMvc.perform(post("/tasks").param("name", "listen to really strange music").param("done", "true"));

        MvcResult res = mockMvc.perform(get("/tasks"))
                .andExpect(model().attributeExists("tasks"))
                .andReturn();
        Collection<Task> tasks = (Collection<Task>) res.getModelAndView().getModel().get("tasks");

        if (tasks.isEmpty()) {
            fail("Toteuta tehtävien lisäys ensin.");
        }

        for (Task task : new ArrayList<>(tasks)) {
            mockMvc.perform(post("/tasks/" + task.getId() + "/delete"))
                    .andExpect(redirectedUrl("/tasks"));
        }
    }

    @Test
    @Points("W1E07.3")
    public void poistoGetillaEiToimi() throws Exception {
        mockMvc.perform(post("/tasks").param("name", "sleep").param("done", "false"));

        MvcResult res = mockMvc.perform(get("/tasks"))
                .andExpect(model().attributeExists("tasks"))
                .andReturn();
        Collection<Task> tasks = (Collection<Task>) res.getModelAndView().getModel().get("tasks");

        if (tasks.isEmpty()) {
            fail("Toteuta tehtävien lisäys ensin.");
        }

        for (Task task : new ArrayList<>(tasks)) {
            mockMvc.perform(get("/tasks/" + task.getId() + "/delete"));
        }

        assertNotEquals("Tehtävien poistamisen ei tule toimia GET-tyyppisillä pyynnöillä.", 0, taskRepository.count());
    }

    @Test
    @Points("W1E07.4")
    public void tehdyksiAsetus() throws Exception {
        for (Task task : taskRepository.findAll()) {
            taskRepository.delete(task);
        }

        for (int i = 0; i < 5; i++) {
            String name = UUID.randomUUID().toString().substring(0, 6);
            mockMvc.perform(post("/tasks").param("name", name).param("done", "false"))
                    .andExpect(redirectedUrl("/tasks"));
        }

        MvcResult res = mockMvc.perform(get("/tasks"))
                .andExpect(model().attributeExists("tasks"))
                .andReturn();
        Collection<Task> tasks = (Collection<Task>) res.getModelAndView().getModel().get("tasks");

        for (Task task : new ArrayList<>(tasks)) {
            mockMvc.perform(post("/tasks/" + task.getId() + "/done"));
        }

        List<Task> taskList = taskRepository.findAll();
        assertEquals("Tehtävien tehdyksi asettamisen ei tule poistaa tehtäviä.", 5, taskList.size());
        for (Task task : taskList) {
            assertTrue("Tehtävä tulee asettaa tehdyksi kun tehdään POST-tyyppinen kutsu osoitteeseen /tasks/{id}/done", task.getDone());
        }
    }

    @Test
    @Points("W1E07.4")
    public void tehdyksiAsetusEiToimiGetilla() throws Exception {
        for (Task task : taskRepository.findAll()) {
            taskRepository.delete(task);
        }

        for (int i = 0; i < 5; i++) {
            String name = UUID.randomUUID().toString().substring(0, 6);
            mockMvc.perform(post("/tasks").param("name", name).param("done", "false"))
                    .andExpect(redirectedUrl("/tasks"));
        }

        MvcResult res = mockMvc.perform(get("/tasks"))
                .andExpect(model().attributeExists("tasks"))
                .andReturn();
        Collection<Task> tasks = (Collection<Task>) res.getModelAndView().getModel().get("tasks");

        for (Task task : new ArrayList<>(tasks)) {
            mockMvc.perform(get("/tasks/" + task.getId() + "/done"));
        }

        List<Task> taskList = taskRepository.findAll();
        assertEquals("Tehtävien tehdyksi asettamisen ei tule poistaa tehtäviä.", 5, taskList.size());
        for (Task task : taskList) {
            assertFalse("Tehtävää ei tule asettaa tehdyksi kun tehdään GET-tyyppinen kutsu osoitteeseen /tasks/{id}/done", task.getDone());
        }
    }
}
