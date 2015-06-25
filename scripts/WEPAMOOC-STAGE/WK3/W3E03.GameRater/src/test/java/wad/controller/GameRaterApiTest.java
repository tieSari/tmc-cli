package wad.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import wad.Application;
import wad.domain.Game;
import wad.domain.Rating;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class GameRaterApiTest {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    private final Random random = new Random();

    private static final String PUBLIC_URL_1
            = "http://wepa-scoreservice-heroku.herokuapp.com/games";

    @Test
    @Points("W3E03.1")
    public void testPostGame() throws Exception {
        String randomGameName = "game:" + UUID.randomUUID().toString();
        postGame(randomGameName);
    }

    @Test
    @Points("W3E03.1")
    public void testPostAndGetGame() throws Exception {
        String randomGameName = "game:" + UUID.randomUUID().toString();
        postGame(randomGameName);
        getGame(randomGameName);
    }

    @Test
    @Points("W3E03.1")
    public void testPostAndGetGameAndListGames() throws Exception {
        String randomGameName = "game:" + UUID.randomUUID().toString();
        postGame(randomGameName);
        Game game = getGame(randomGameName);
        expectToFindGameFromList(game.getId(), randomGameName);
    }

    @Test
    @Points("W3E03.1")
    public void testPostAndGetGameAndListGamesAndDeleteGame() throws Exception {
        String randomGameName = "game:" + UUID.randomUUID().toString();
        postGame(randomGameName);
        Game g = getGame(randomGameName);
        Long id = g.getId();
        expectToFindGameFromList(id, randomGameName);
        deleteGame(randomGameName);
        expectNotToFindGameFromList(id);
    }

    @Test
    @Points("W3E03.1")
    public void testIfCodeActuallyUsesHighFiveServiceOverNetworkToFetchGames() throws Exception {
        String randomGameName = "public:game:" + UUID.randomUUID().toString();
        Game g = new Game();
        g.setName(randomGameName);

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.postForEntity(PUBLIC_URL_1, g, Game.class);

        try {
            getGame(randomGameName);
        } catch (Throwable t) {
            fail("Verify that your application actually connects to one of the public High Five servers to fetch games. Error: "
                    + t.toString());
        }
    }

    @Test
    @Points("W3E03.2")
    public void testPostGameAndACoupleOfRatings() throws Exception {
        String randomGameName = "game:" + UUID.randomUUID().toString();
        postGame(randomGameName);

        int[] ratings = new int[2];
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = random.nextInt(6);
        }
        postRatings(randomGameName, ratings);
    }

    @Test
    @Points("W3E03.2")
    public void testPostGameAndACoupleOfRatingsAndGetRatings() throws Exception {
        String randomGameName = "game:" + UUID.randomUUID().toString();
        postGame(randomGameName);

        int[] ratings = new int[3];
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = random.nextInt(6);
        }

        long[] ratingIds = postRatings(randomGameName, ratings);

        for (int i = 0; i < ratings.length; i++) {
            getRating(randomGameName, ratingIds[i], ratings[i]);
        }
    }

    @Test
    @Points("W3E03.2")
    public void testPostGameAndACoupleOfRatingsAndListRatings() throws Exception {
        String randomGameName = "game:" + UUID.randomUUID().toString();
        postGame(randomGameName);

        int[] ratings = new int[5];
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = random.nextInt(6);
        }
        long[] ratingIds = postRatings(randomGameName, ratings);

        getRatings(randomGameName);
        expectToFindRatingFromList(randomGameName, ratingIds, ratings);
    }

    @Test
    @Points("W3E03.2")
    public void testPostGameAndACoupleOfRatingsAndListRatingsAndDeleteSpecificRatings() throws Exception {
        String randomGameName = "game:" + UUID.randomUUID().toString();
        postGame(randomGameName);

        int[] ratings = new int[7];
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = random.nextInt(6);
        }
        long[] ratingIds = postRatings(randomGameName, ratings);

        deleteRating(randomGameName, ratingIds[0]);
        deleteRating(randomGameName, ratingIds[2]);
        deleteRating(randomGameName, ratingIds[5]);

        expectNotToFindRatingFromList(randomGameName, new long[]{ratingIds[0], ratingIds[2], ratingIds[5]});
    }

    public long[] postRatings(String gameName, int[] ratings) throws Exception {
        long[] ids = new long[ratings.length];
        for (int i = 0; i < ratings.length; i++) {
            Rating rating = postRating(gameName, ratings[i]);
            ids[i] = rating.getId();
        }
        return ids;
    }

    protected void expectToFindRatingFromList(String gameName, long[] expectedIds, int[] expectedRatings) throws Exception {
        List<Rating> ratings = getRatings(gameName);

        for (Rating rating : ratings) {

            boolean foundId = false;
            for (int i = 0; i < expectedIds.length; i++) {
                long expectedId = expectedIds[i];
                int expectedRating = expectedRatings[i];
                if (new Long(expectedId).equals(rating.getId())) {
                    foundId = true;
                    assertEquals("GET-request to /games/" + gameName + "/ratings returns incorrect rating for the game",
                            expectedRating, rating.getRating());
                    break;
                }
            }
            if (!foundId) {
                fail("Cannot find a stored rating in the JSON array response of GET-request to /games/" + gameName + "/ratings");
            }
        }
    }

    protected void expectNotToFindRatingFromList(String gameName, long[] expectedIds) throws Exception {
        List<Rating> ratings = getRatings(gameName);

        for (Rating rating : ratings) {
            boolean foundId = false;

            for (int i = 0; i < expectedIds.length; i++) {
                long expectedId = expectedIds[i];
                if (new Long(expectedId).equals(rating.getId())) {
                    foundId = true;
                    break;
                }
            }
            if (foundId) {
                fail("Expected not to find a rating after deletion in the response of GET-request to /games/" + gameName + "/ratings");
            }

        }
    }

    protected Rating postRating(String gameName, int rating) throws Exception {

        Rating r = new Rating();
        r.setRating(rating);

        Gson gson = new Gson();
        String ratingJson = gson.toJson(r);

        MvcResult res = mockMvc.perform(
                post("/games/{gameName}/ratings", gameName).contentType(MediaType.APPLICATION_JSON).content(ratingJson)
        ).andExpect(status().isOk()).andReturn();

        return gson.fromJson(res.getResponse().getContentAsString(), Rating.class);
    }

    protected Rating getRating(String gameName, Long ratingId, int expectedRating) throws Exception {

        MvcResult res = mockMvc.perform(get("/games/{gameName}/ratings/{ratingId}", gameName, ratingId)).andExpect(status().isOk()).andReturn();

        Rating r = new Gson().fromJson(res.getResponse().getContentAsString(), Rating.class);

        assertEquals(ratingId, r.getId());
        assertEquals(expectedRating, r.getRating());

        return r;
    }

    protected Rating deleteRating(String gameName, Long ratingId) throws Exception {

        MvcResult res = mockMvc.perform(delete("/games/{gameName}/ratings/{ratingId}", gameName, ratingId)).andExpect(status().isOk()).andReturn();

        return new Gson().fromJson(res.getResponse().getContentAsString(), Rating.class);
    }

    protected List<Rating> getRatings(String gameName) throws Exception {

        MvcResult res = mockMvc.perform(get("/games/{gameName}/ratings", gameName)).andExpect(status().isOk()).andReturn();
        return new Gson().fromJson(
                res.getResponse().getContentAsString(),
                new TypeToken<ArrayList<Rating>>() {
                }.getType());
    }

    protected void expectToFindGameFromList(Long expectedId, String expectedName) throws Exception {
        List<Game> games = getGames();

        Game matchingGame = null;
        for (Game game : games) {
            if (expectedId.equals(game.getId())) {
                matchingGame = game;
                break;
            }
        }

        if (matchingGame == null) {
            fail("Cannot find a stored game in the response of GET-request to /games");
            return;
        }

        assertEquals("GET-request to /games returns incorrect name for game",
                expectedName, matchingGame.getName());
    }

    protected void expectNotToFindGameFromList(Long expectedId) throws Exception {
        List<Game> games = getGames();

        Game matchingGame = null;

        for (Game game : games) {
            if (expectedId.equals(game.getId())) {
                matchingGame = game;
                break;
            }
        }

        if (matchingGame != null) {
            fail("Expected not to find a game after deletion in the response of GET-request to /games");
        }
    }

    protected Game postGame(String name) throws Exception {
        Game g = new Game();
        g.setName(name);

        Gson gson = new Gson();
        String gameJson = gson.toJson(g);

        MvcResult res = mockMvc.perform(
                post("/games").contentType(MediaType.APPLICATION_JSON).content(gameJson)
        ).andExpect(status().isOk()).andReturn();

        Game game = gson.fromJson(res.getResponse().getContentAsString(), Game.class);

        assertNotNull("Game should exist after creation; now game " + name + " was unavailable in database.", game);
        assertNotNull("Game should have an id, now created game had id " + game.getId(), game.getId());

        return game;
    }

    protected Game getGame(String name) throws Exception {

        MvcResult res = mockMvc.perform(get("/games/{name}", name)).andExpect(status().isOk()).andReturn();

        Gson gson = new Gson();
        Game game = gson.fromJson(res.getResponse().getContentAsString(), Game.class);

        assertNotNull("When retrieving a game through the /games/{name} -address, game was not found. Verify that the GET-request works.", game);
        assertNotNull("When retrieving a game through the /games/{name} -address, the retrieved game should have the database id. Now the id was " + game.getId(), game.getId());
        assertEquals("When retrieving a game through the /games/{name} -address with name \"" + name + "\", the retrieved game should have the name \"" + name + "\". Now the name was " + game.getName(), name, game.getName());

        return game;
    }

    protected Game deleteGame(String name) throws Exception {

        MvcResult res = mockMvc.perform(delete("/games/{name}", name)).andExpect(status().isOk()).andReturn();

        Gson gson = new Gson();
        Game game = gson.fromJson(res.getResponse().getContentAsString(), Game.class);

        assertNotNull("When deleting a game through /games/{name} -address, game was not found. Verify that the DELETE-method works.", game);
        assertNotNull("When deleting a game through /games/{name} -address, the retrieved game should have the database id of the old deleted game. Now the id was " + game.getId(), game.getId());
        assertEquals("When deleting a game through /games/{name} -address with name \"" + name + "\", the retrieved game should have the name of the old deleted game \"" + name + "\". Now the name was " + game.getName(), name, game.getName());

        Game deleted = null;
        try {
            deleted = getGame(name);
        } catch (Throwable t) {

        }

        assertNull("Game should not exist after deletion; now game " + name + " was available in database.", deleted);

        return deleted;
    }

    protected List<Game> getGames() throws Exception {
        MvcResult res = mockMvc.perform(get("/games")).andExpect(status().isOk()).andReturn();

        return new Gson().fromJson(
                res.getResponse().getContentAsString(),
                new TypeToken<ArrayList<Game>>() {
                }.getType());
    }

}
