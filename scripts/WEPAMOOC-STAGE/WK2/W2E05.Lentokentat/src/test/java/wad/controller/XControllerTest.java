package wad.controller;

import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wad.Application;
import wad.domain.Aircraft;
import wad.domain.Airport;
import wad.repository.AircraftRepository;
import wad.repository.AirportRepository;

@Points("W2E05.2")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
@WebAppConfiguration
public class XControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private AirportRepository airportRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void urlIsListened() throws Throwable {
        aircraftRepository.deleteAll();
        Aircraft ac = new Aircraft();
        ac.setName("HA-L0L");
        ac = aircraftRepository.save(ac);

        Airport ap = new Airport();
        ap.setName("Batman Airport");
        ap = airportRepository.save(ap);

        mockMvc.perform(post("/aircrafts/" + ac.getId() + "/airports")
                .param("airportId", "" + ap.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/aircrafts"));

        ac = aircraftRepository.findOne(ac.getId());

        Airport airport = ReflectionUtils.invokeMethod(Airport.class, ReflectionUtils.requireMethod(Aircraft.class, "getAirport"), ac);
        
        assertEquals("When a POST is made to /airports/{aircraftId}/airports, the posted airport should be set as the home airport of the aircraft with {aircraftId}.", ap.getId(), airport.getId());

        ap = airportRepository.findOne(ap.getId());
        List<Aircraft> aircrafts = ReflectionUtils.invokeMethod(List.class, ReflectionUtils.requireMethod(Airport.class, "getAircrafts"), ap);
        
        boolean found = false;
        try {
            for (Aircraft aircraft : aircrafts) {
                if (aircraft.getId().equals(ac.getId())) {
                    found = true;
                    break;
                }
            }
        } catch (Throwable t) {
            fail("Onhan airport-luokan @OneToMany-annotaatiolla FetchType.EAGER-määrittely?");
        }

        assertTrue("When a POST is made to /airports/{aircraftId}/airports, the posted airport should contain the aircraft with {aircraftId}.", found);
    }
}
