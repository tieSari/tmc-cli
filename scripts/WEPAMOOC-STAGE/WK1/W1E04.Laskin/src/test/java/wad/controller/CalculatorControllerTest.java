package wad.controller;

import fi.helsinki.cs.tmc.edutestutils.Points;
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
import wad.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Points("W1E04")
public class CalculatorControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void palauttaaNollaOletuksena() throws Exception {
        mockMvc.perform(get("/calculate"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    @Test
    public void plusOletusLasku() throws Exception {
        mockMvc.perform(get("/calculate").param("first", "3").param("second", "8"))
                .andExpect(status().isOk())
                .andExpect(content().string("11"));
    }

    @Test
    public void plusOletusLasku2() throws Exception {
        mockMvc.perform(get("/calculate").param("first", "5").param("second", "8"))
                .andExpect(status().isOk())
                .andExpect(content().string("13"));
    }

    @Test
    public void plusParametrina() throws Exception {
        mockMvc.perform(get("/calculate").param("op", "plus").param("first", "1").param("second", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    public void minus() throws Exception {
        mockMvc.perform(get("/calculate").param("op", "minus").param("first", "5").param("second", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    @Test
    public void minusNegat() throws Exception {
        mockMvc.perform(get("/calculate").param("op", "minus").param("first", "-35").param("second", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("-37"));
    }

    @Test
    public void multiply() throws Exception {
        mockMvc.perform(get("/calculate").param("op", "multiply").param("first", "6").param("second", "7"))
                .andExpect(status().isOk())
                .andExpect(content().string("42"));
    }

    @Test
    public void multiply2() throws Exception {
        mockMvc.perform(get("/calculate").param("op", "multiply").param("first", "11").param("second", "7"))
                .andExpect(status().isOk())
                .andExpect(content().string("77"));
    }
}
