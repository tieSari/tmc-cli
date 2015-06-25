package wad.controller;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wad.Application;
import wad.domain.Actor;
import wad.domain.Movie;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
@WebAppConfiguration
public class MovieDatabaseTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Points("W2E06.1")
    public void testActors() throws Exception {
        // listaus
        MvcResult res = mockMvc.perform(get("/actors"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("actors"))
                .andExpect(view().name("/WEB-INF/views/actors.jsp"))
                .andReturn();

        List<Actor> actors = new ArrayList<>((Collection<Actor>) res.getModelAndView().getModel().get("actors"));
        int actorCount = actors.size();

        // lisäys
        String rnd = UUID.randomUUID().toString().substring(0, 4);
        String name = "van damme " + rnd;
        mockMvc.perform(post("/actors").param("name", name))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        res = mockMvc.perform(get("/actors"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("actors"))
                .andExpect(view().name("/WEB-INF/views/actors.jsp"))
                .andReturn();

        actors = new ArrayList<>((Collection<Actor>) res.getModelAndView().getModel().get("actors"));
        assertTrue("Kun yksi näyttelijä lisätään, tulee näyttelijöiden määrän kasvaa vain yhdellä.", actorCount + 1 == actors.size());
        boolean found = false;
        Long id = -1L;
        for (Actor actor : actors) {
            if (actor.getName() == null) {
                continue;
            }

            if (actor.getName().equals(name)) {
                id = actor.getId();
                found = true;
                break;
            }
        }

        assertTrue("Kun osoitteeseen /actors tehdään POST-pyyntö, missä on parametri 'name', parametrin pohjalta tulee luoda uusi näyttelijä. Nyt näyttelijää ei luotu tai sitä ei löytynyt /actors-osoitteeseen tehdyn GET-pyynnön vastauksesta.", found);

        // poistetaan ja tarkistetaan että poistettu
        mockMvc.perform(delete("/actors/" + id))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        res = mockMvc.perform(get("/actors"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("actors"))
                .andExpect(view().name("/WEB-INF/views/actors.jsp"))
                .andReturn();

        actors = new ArrayList<>((Collection<Actor>) res.getModelAndView().getModel().get("actors"));
        assertTrue("Kun yksi näyttelijä lisätään ja sitten poistetaan, näyttelijöiden kokonaismäärän ei tule muuttua.", actorCount == actors.size());

        found = false;
        for (Actor actor : actors) {
            if (actor.getName() == null) {
                continue;
            }

            if (actor.getName().equals(name)) {
                found = true;
                break;
            }
        }

        assertFalse("Kun osoitteeseen /actors/{id} tehdään DELETE-pyyntö, missä on {id} on näyttelijän tunnus, tulee näyttelijä poistaa. Nyt näyttelijää ei poistettu (se löytyi poiston jälkeen /actors-osoitteeseen tehdyn GET-pyynnön vastauksesta).", found);
    }

    @Test
    @Points("W2E06.2")
    public void testMovies() throws Exception {
        // listaus
        MvcResult res = mockMvc.perform(get("/movies"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("movies"))
                .andExpect(view().name("/WEB-INF/views/movies.jsp"))
                .andReturn();

        List<Movie> movies = new ArrayList<>((Collection<Movie>) res.getModelAndView().getModel().get("movies"));
        int movieCount = movies.size();

        // lisäys
        String rnd = UUID.randomUUID().toString().substring(0, 4);
        int pituus = new Random().nextInt(20) + 10;
        String name = "bloodsport " + rnd;

        Long movieId = createMovie(name, pituus);

        res = mockMvc.perform(get("/movies"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("movies"))
                .andExpect(view().name("/WEB-INF/views/movies.jsp"))
                .andReturn();

        movies = new ArrayList<>((Collection<Movie>) res.getModelAndView().getModel().get("movies"));
        assertTrue("Kun yksi elokuva lisätään, tulee elokuvien määrän kasvaa vain yhdellä.", movieCount + 1 == movies.size());
        boolean found = false;
        Long id = -1L;
        for (Movie movie : movies) {
            if (movie.getName() == null) {
                continue;
            }

            if (movie.getLengthInMinutes() == null) {
                continue;
            }

            if (movie.getName().equals(name) && movie.getLengthInMinutes() == pituus) {
                id = movie.getId();
                found = true;
                break;
            }
        }

        assertTrue("Kun osoitteeseen /movies tehdään POST-pyyntö, missä on parametri 'name' ja 'lengthInMinutes', parametrien pohjalta tulee luoda uusi elokuva. Nyt elokuvaa ei luotu tai sitä ei löytynyt /movies-osoitteeseen tehdyn GET-pyynnön vastauksesta.", found);

        // poistetaan ja tarkistetaan että poistettu
        mockMvc.perform(delete("/movies/" + id))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        res = mockMvc.perform(get("/movies"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("movies"))
                .andExpect(view().name("/WEB-INF/views/movies.jsp"))
                .andReturn();

        movies = new ArrayList<>((Collection<Movie>) res.getModelAndView().getModel().get("movies"));
        assertTrue("Kun yksi elokuva lisätään ja sitten poistetaan, elokuvien kokonaismäärän ei tule muuttua.", movieCount == movies.size());

        found = false;
        for (Movie movie : movies) {
            if (movie.getName() == null) {
                continue;
            }

            if (movie.getName().equals(name)) {
                found = true;
                break;
            }
        }

        assertFalse("Kun osoitteeseen /movies/{id} tehdään DELETE-pyyntö, missä on {id} on elokuvan tunnus, tulee elokuva poistaa. Nyt elokuvaa ei poistettu (se löytyi poiston jälkeen /movies-osoitteeseen tehdyn GET-pyynnön vastauksesta).", found);
    }

    @Test
    @Points("W2E06.3 W2E06.4")
    public void testAssigningMoviesAndRetrievingSingleUsers() throws Exception {

        String name = "van damme " + UUID.randomUUID().toString().substring(0, 4);
        Long actorId = createActor(name);
        // actor created, lets see if we can retrieve it
        MvcResult res = mockMvc.perform(get("/actors/" + actorId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("movies"))
                .andExpect(model().attributeExists("actor"))
                .andExpect(view().name("/WEB-INF/views/actor.jsp"))
                .andReturn();

        Actor actor = (Actor) res.getModelAndView().getModel().get("actor");
        assertEquals("Kun osoitteeseen /actors/{actorId} tehdään kutsu, tulee actor-attribuutissa palautettavan näyttelijän olla tietokantaan tunnuksella 'actorId' tallennettu näyttelijä.", name, actor.getName());
        assertEquals("Kun uusi näyttelijä on luotu, hänellä ei pitäisi olla vielä yhtäkään elokuvaa.", 0, actor.getMovies().size());

        int pituus = new Random().nextInt(20) + 10;
        name = "bloodsport " + UUID.randomUUID().toString().substring(0, 4);

        Long bloodsportId = createMovie(name, pituus);

        name = "kickboxer " + UUID.randomUUID().toString().substring(0, 4);
        Long kickboxerId = createMovie(name, pituus);

        mockMvc.perform(post("/actors/" + actorId + "/movies").param("movieId", "" + bloodsportId));
        
        res = mockMvc.perform(get("/actors/" + actorId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("movies"))
                .andExpect(model().attributeExists("actor"))
                .andExpect(view().name("/WEB-INF/views/actor.jsp"))
                .andReturn();

        actor = (Actor) res.getModelAndView().getModel().get("actor");
        assertEquals("Kun osoitteeseen /actors/{actorId}/movies tehdään kutsu, jossa on elokuvan tunnus 'movieId', tulee elokuva lisätä näyttelijälle.", 1, actor.getMovies().size());
        assertEquals("Kun näyttelijälle lisätään elokuva, tulee elokuvalle lisätä myös näyttelijä.", 1, actor.getMovies().get(0).getActors().size());
        
        // poistetaan elokuva
        mockMvc.perform(delete("/movies/" + bloodsportId))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        
        
        res = mockMvc.perform(get("/actors/" + actorId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("movies"))
                .andExpect(model().attributeExists("actor"))
                .andExpect(view().name("/WEB-INF/views/actor.jsp"))
                .andReturn();

        actor = (Actor) res.getModelAndView().getModel().get("actor");
        assertEquals("Kun elokuva poistetaan, tulee viite elokuvaan poistaa myös näyttelijältä.", 0, actor.getMovies().size());
        
    }

    private Long createMovie(String name, Integer length) throws Exception {
        mockMvc.perform(post("/movies").param("name", name).param("lengthInMinutes", "" + length))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        MvcResult res = mockMvc.perform(get("/movies"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("movies"))
                .andExpect(view().name("/WEB-INF/views/movies.jsp"))
                .andReturn();

        List<Movie> movies = new ArrayList<>((Collection<Movie>) res.getModelAndView().getModel().get("movies"));

        for (Movie movie : movies) {
            if (movie.getName() == null) {
                continue;
            }

            if (movie.getLengthInMinutes() == null) {
                continue;
            }

            if (movie.getName().equals(name) && movie.getLengthInMinutes() == length) {
                if (movie.getActors().size() > 0) {
                    fail("Kun uusi elokuva on luotu, sillä ei pitäisi olla vielä yhtäkään näyttelijää.");
                }

                return movie.getId();
            }
        }

        return -1L;
    }

    private Long createActor(String name) throws Exception {
        mockMvc.perform(post("/actors").param("name", name))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        MvcResult res = mockMvc.perform(get("/actors"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("actors"))
                .andExpect(view().name("/WEB-INF/views/actors.jsp"))
                .andReturn();

        List<Actor> actors = new ArrayList<>((Collection<Actor>) res.getModelAndView().getModel().get("actors"));

        for (Actor actor : actors) {
            if (actor.getName() == null) {
                continue;
            }

            if (actor.getName().equals(name)) {
                if (actor.getMovies().size() > 0) {
                    fail("Kun uusi näyttelijä on luotu, sillä ei pitäisi olla vielä yhtäkään elokuvaa.");
                }

                return actor.getId();
            }
        }

        return -1L;
    }

//
//    @Test
//    @Points("W2E06.1")
//    public void testAddAndRemoveActor() throws Exception {
//        driver.get(baseUrl + "/app/actor/");
//        assertNotPresent("Van Damme");
//        driver.findElement(By.id("name")).clear();
//        driver.findElement(By.id("name")).sendKeys("Van Damme");
//        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
//        assertPresent("Van Damme");
//        driver.findElement(By.xpath("//li/form/input")).click();
//        assertNotPresent("Van Damme");
//        assertNotPresent("Chuck Norris");
//        driver.findElement(By.id("name")).clear();
//        driver.findElement(By.id("name")).sendKeys("Chuck Norris");
//        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
//        assertNotPresent("Van Damme");
//        assertPresent("Chuck Norris");
//        driver.findElement(By.id("name")).clear();
//        driver.findElement(By.id("name")).sendKeys("Van Damme");
//        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
//        assertPresent("Van Damme");
//        assertPresent("Chuck Norris");
//        driver.findElement(By.xpath("//li[2]/form/input")).click();
//        assertNotPresent("Van Damme");
//        assertPresent("Chuck Norris");
//        driver.findElement(By.xpath("//li[1]/form/input")).click();
//        assertNotPresent("Van Damme");
//        assertNotPresent("Chuck Norris");
//    }
//
//    @Test
//    @Points("W2E06.2")
//    public void testAddAndRemoveMovie() throws Exception {
//        driver.get(baseUrl + "/app/movie/");
//        assertNotPresent("Bloodsport");
//        driver.findElement(By.id("name")).clear();
//        driver.findElement(By.id("name")).sendKeys("Bloodsport");
//        driver.findElement(By.id("lengthInMinutes")).clear();
//        driver.findElement(By.id("lengthInMinutes")).sendKeys("92");
//        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
//        assertPresent("Bloodsport");
//        driver.findElement(By.xpath("//li/form/input")).click();
//        assertNotPresent("Bloodsport");
//        driver.findElement(By.id("name")).clear();
//        driver.findElement(By.id("name")).sendKeys("Bloodsport");
//        driver.findElement(By.id("lengthInMinutes")).clear();
//        driver.findElement(By.id("lengthInMinutes")).sendKeys("92");
//        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
//        assertPresent("Bloodsport");
//        driver.findElement(By.id("name")).clear();
//        driver.findElement(By.id("name")).sendKeys("Sidekicks");
//        driver.findElement(By.id("lengthInMinutes")).clear();
//        driver.findElement(By.id("lengthInMinutes")).sendKeys("101");
//        driver.findElement(By.xpath("//input[@type='submit']")).click();
//        assertPresent("Bloodsport");
//        assertPresent("Sidekicks");
//        driver.findElement(By.xpath("//li[2]/form/input")).click();
//        assertPresent("Bloodsport");
//        assertNotPresent("Sidekicks");
//        driver.findElement(By.xpath("//li[1]/form/input")).click();
//        assertNotPresent("Bloodsport");
//        assertNotPresent("Sidekicks");
//    }
//
//    @Test
//    @Points("W2E06.3 W2E06.4")
//    public void testAddMovieAndActorAndAssignFinallyRemoveAll() throws Exception {
//        driver.get(baseUrl + "/app/movie/");
//        assertNotPresent("Bloodsport");
//        assertNotPresent("Van Damme");
//        driver.findElement(By.id("name")).clear();
//        driver.findElement(By.id("name")).sendKeys("Bloodsport");
//        driver.findElement(By.id("lengthInMinutes")).clear();
//        driver.findElement(By.id("lengthInMinutes")).sendKeys("92");
//        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
//        assertPresent("Bloodsport");
//        assertNotPresent("Van Damme");
//        driver.findElement(By.linkText("Actors")).click();
//        assertNotPresent("Van Damme");
//        driver.findElement(By.id("name")).clear();
//        driver.findElement(By.id("name")).sendKeys("Van Damme");
//        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
//        assertPresent("Van Damme");
//        driver.findElement(By.linkText("Van Damme")).click();
//        driver.findElement(By.id("add-to-movie")).click();
//        driver.findElement(By.linkText("Movies")).click();
//        assertPresent("Bloodsport");
//        assertPresent("Van Damme");
//        driver.findElement(By.xpath("//li[1]/form/input")).click();
//        assertNotPresent("Bloodsport");
//        assertNotPresent("Van Damme");
//        driver.findElement(By.linkText("Actors")).click();
//        assertPresent("Van Damme");
//        assertNotPresent("Bloodsport");
//        assertNotPresent("null");
//        driver.findElement(By.xpath("//li/form/input")).click();
//        assertNotPresent("Van Damme");
//        assertNotPresent("Bloodsport");
//        assertNotPresent("null");
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        driver.quit();
//    }
//
//    private void assertPresent(String text) {
//        assertTrue("The page should contain text: " + text + "\nCurrent source:\n" + driver.getPageSource(), isTextPresent(text));
//    }
//
//    private void assertNotPresent(String text) {
//        assertFalse("The page should not contain text: " + text + "\nCurrent source:\n" + driver.getPageSource(), isTextPresent(text));
//    }
//
//    private boolean isTextPresent(String text) {
//        try {
//            return driver.getPageSource().contains(text);
//        } catch (Exception e) {
//            return false;
//        }
//    }
}
