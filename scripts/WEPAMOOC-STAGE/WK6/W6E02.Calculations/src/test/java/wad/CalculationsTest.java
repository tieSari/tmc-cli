package wad;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wad.domain.Calculation;
import wad.repository.CalculationRepository;
import wad.service.CalculationService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
@Points("W6E02")
public class CalculationsTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private CalculationRepository calculationRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void postAddsCalculation() throws Exception {
        calculationRepository.deleteAll();

        MvcResult res = mockMvc.perform(post("/calculations").param("content", "elaemaen salaisuus"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        List<Calculation> calculations = calculationRepository.findAll();

        assertTrue(calculations.size() == 1);
        assertEquals("elaemaen salaisuus", calculations.get(0).getContent());
        assertEquals("PROCESSING", calculations.get(0).getStatus());

        try {
            Thread.sleep(2500);
        } catch (Throwable t) {

        }

        calculations = calculationRepository.findAll();

        assertTrue(calculations.size() == 1);
        assertEquals("elaemaen salaisuus", calculations.get(0).getContent());
        assertEquals("PROCESSED", calculations.get(0).getStatus());

    }

    @Test
    public void statusChangesToProcessedAfterTime() throws Exception {
        calculationRepository.deleteAll();

        MvcResult res = mockMvc.perform(post("/calculations").param("content", "onko vesilasi puolityhjae vai puolitaeysi"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        List<Calculation> calculations = calculationRepository.findAll();

        assertTrue(calculations.size() == 1);
        assertEquals("onko vesilasi puolityhjae vai puolitaeysi", calculations.get(0).getContent());
        assertEquals("PROCESSING", calculations.get(0).getStatus());
        assertNull(calculations.get(0).getCalculationResult());

        try {
            Thread.sleep(2500);
        } catch (Exception e) {

        }

        calculations = calculationRepository.findAll();

        assertTrue(calculations.size() == 1);
        assertEquals("onko vesilasi puolityhjae vai puolitaeysi", calculations.get(0).getContent());
        assertEquals("PROCESSED", calculations.get(0).getStatus());
        assertNotNull(calculations.get(0).getCalculationResult());

    }
}
