package wad.controller;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.ArrayList;
import javax.transaction.Transactional;
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

import static org.junit.Assert.*;

import wad.Application;
import wad.domain.Album;
import wad.domain.Track;

import wad.repository.AlbumRepository;
import wad.repository.TrackRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AlbumControllerTest {
    
    @Autowired
    private WebApplicationContext webAppContext;
    
    @Autowired
    private AlbumRepository albumRepository;
    
    @Autowired
    private TrackRepository trackRepository;
    
    private MockMvc mockMvc;
    
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }
    
    @Test
    @Points("W1E08.1")
    @Transactional
    public void kappaleenLisays() throws Exception {
        Album album = new Album();
        album.setName("You Can Tune a Piano, But You Can't Tuna Fish");
        album.setTracks(new ArrayList<Track>());
        album = albumRepository.save(album);
        
        if (album.getTracks() != null) {
            for (Track track : album.getTracks()) {
                if (track.getName().contains("o-noess")) {
                    fail("Albumilla ei tule olla kappaletta \"o-noess\" ennen kappaleen lisäämistä.");
                }
            }
        }
        
        mockMvc.perform(post("/albums/" + album.getId() + "/tracks").param("name", "o-noess"))
                .andExpect(redirectedUrl("/albums"));
        
        album = albumRepository.findOne(album.getId());
        
        boolean loytyi = false;
        for (Track track : album.getTracks()) {
            if (track.getName().contains("o-noess")) {
                loytyi = true;
            }
        }
        
        if (!loytyi) {
            fail("Kun kappale lisätään, se tulee löytyä albumilta jolle se on lisätty.");
        }
    }
    
    @Test
    @Points("W1E08.2")
    @Transactional
    public void kappaleenPoisto() throws Exception {
        kappaleenLisays();
        
        Album album = albumRepository.findAll().get(0);
        assertTrue("Albumilla johon on lisätty kappale tulee olla vähintään yksi kappale.", album.getTracks().size() > 0);
        for (Track track : album.getTracks()) {
            assertNotNull("Tallennathan kappaleet? Nyt kappaleella ei ollut tietokannan luomaa tunnusta.", track.getId());
        }
        
        for (Track track : new ArrayList<>(album.getTracks())) {
            mockMvc.perform(post("/albums/" + album.getId() + "/tracks/" + track.getId() + "/delete"))
                    .andExpect(redirectedUrl("/albums"));
        }
        
        album = albumRepository.findOne(album.getId());
        
        assertTrue("Kun kappaleet on poistettu yksitellen, albumilla ei tule olla enää yhtäkään kappaletta.", album.getTracks().isEmpty());
    }
    
    @Test
    @Points("W1E08.2")
    @Transactional
    public void kappaleenPoistoEiOnnistuGetilla() throws Exception {
        kappaleenLisays();
        
        Album album = albumRepository.findAll().get(0);
        assertTrue("Albumilla johon on lisätty kappale tulee olla vähintään yksi kappale.", album.getTracks().size() > 0);
        for (Track track : album.getTracks()) {
            assertNotNull("Tallennathan kappaleet? Nyt kappaleella ei ollut tietokannan luomaa tunnusta.", track.getId());
        }
        
        for (Track track : new ArrayList<>(album.getTracks())) {
            mockMvc.perform(get("/albums/" + album.getId() + "/tracks/" + track.getId() + "/delete"));
        }
        
        album = albumRepository.findOne(album.getId());
        
        assertFalse("Kappaleiden poistamisen ei pitäisi onnistua GET-tyyppisillä pyynnöillä.", album.getTracks().isEmpty());
    }
}
