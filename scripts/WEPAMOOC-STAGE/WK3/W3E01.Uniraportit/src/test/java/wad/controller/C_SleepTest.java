package wad.controller;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Map;
import java.util.UUID;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.*;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Points("W3E01.2 W3E01.3")
public class C_SleepTest {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void getSleeps() throws Exception {
        mockMvc.perform(get("/sleeps"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/sleeps.jsp"))
                .andExpect(model().attributeExists("sleeps"));

    }

    @Test
    public void redirectOnSuccessfulPost() throws Exception {

        String feeling = UUID.randomUUID().toString().substring(0, 10);

        MvcResult res = mockMvc.perform(post("/sleeps")
                .param("feeling", feeling)
                .param("start", "1.9.2014 22.30")
                .param("end", "2.9.2014 06.30"))
                .andExpect(status().is3xxRedirection()).andReturn();

        Long id = getAttributeId(res.getModelAndView().getModel());

        assertTrue("Verify that you use RedirectAttributes and add the generated sleep id to the attribute. Now there was no Long-type attribute value.", id > 0);
        assertTrue("Verify that you use RedirectAttributes and add the generated sleep id to the attribute. The redirect url should be of form '/sleeps/{attributeId}'.", res.getModelAndView().getViewName().contains("{"));
        assertTrue("Verify that you use RedirectAttributes and add the generated sleep id to the attribute. The redirect url should be of form '/sleeps/{attributeId}'.", res.getModelAndView().getViewName().contains("}"));
    }

    @Test
    public void newSleepAvailableOnCreation() throws Exception {
        String feeling = UUID.randomUUID().toString().substring(0, 10);

        MvcResult res = mockMvc.perform(post("/sleeps")
                .param("feeling", feeling)
                .param("start", "1.9.2014 22.30")
                .param("end", "2.9.2014 06.30"))
                .andExpect(status().is3xxRedirection()).andReturn();

        Long id = getAttributeId(res.getModelAndView().getModel());

        assertTrue("Verify that you use RedirectAttributes and add the generated sleep id to the attribute. Now there was no Long-type attribute value.", id > 0);
        assertTrue("Verify that you use RedirectAttributes and add the generated sleep id to the attribute. The redirect url should be of form '/sleeps/{attributeId}'.", res.getModelAndView().getViewName().contains("{"));
        assertTrue("Verify that you use RedirectAttributes and add the generated sleep id to the attribute. The redirect url should be of form '/sleeps/{attributeId}'.", res.getModelAndView().getViewName().contains("}"));

        mockMvc.perform(get("/sleeps/{id}", id))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("sleep"))
                .andExpect(model().attribute("sleep", hasProperty("feeling", equalTo(feeling))))
                .andExpect(forwardedUrl("/WEB-INF/views/sleep.jsp"));
    }

    @Test
    public void returnToFormWhenNoFeeling() throws Exception {
        try {
            mockMvc.perform(post("/sleeps")
                    .param("feeling", "")
                    .param("start", "1.9.2014 22.30")
                    .param("end", "2.9.2014 06.30"))
                    .andExpect(status().isOk())
                    .andExpect(forwardedUrl("/WEB-INF/views/sleeps.jsp"));
        } catch (Throwable t) {
            fail("When the feeling is empty, the user should be shown the form again with existing inputs and error messages. Error: " + t.getMessage());
        }
    }

    @Test
    public void returnToFormWhenNoStartDate() throws Exception {
        try {
            mockMvc.perform(post("/sleeps")
                    .param("feeling", "fresh")
                    .param("start", "")
                    .param("end", "2.9.2014 06.30"))
                    .andExpect(status().isOk())
                    .andExpect(forwardedUrl("/WEB-INF/views/sleeps.jsp"));
        } catch (Throwable t) {
            fail("When the start date is invalid, the user should be shown the form again with existing inputs and error messages. Error: " + t.getMessage());
        }
    }

    @Test
    public void returnToFormWhenNoEndDate() throws Exception {
        try {
            mockMvc.perform(post("/sleeps")
                    .param("feeling", "fresh")
                    .param("start", "1.9.2014 22.30")
                    .param("end", ""))
                    .andExpect(status().isOk())
                    .andExpect(forwardedUrl("/WEB-INF/views/sleeps.jsp"));
        } catch (Throwable t) {
            fail("When the end date is invalid, the user should be shown the form again with existing inputs and error messages. Error: " + t.getMessage());
        }
    }

    @Test
    public void deleteRedirects() throws Exception {
        String feeling = UUID.randomUUID().toString().substring(0, 6);
        
        MvcResult res = mockMvc.perform(post("/sleeps")
                .param("feeling", feeling)
                .param("start", "1.9.2014 22.30")
                .param("end", "2.9.2014 06.30"))
                .andExpect(status().is3xxRedirection()).andReturn();
        
        Long id = getAttributeId(res.getModelAndView().getModel());

        
        mockMvc.perform(get("/sleeps/{id}", id))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("sleep"))
                .andExpect(model().attribute("sleep", hasProperty("feeling", equalTo(feeling))))
                .andExpect(forwardedUrl("/WEB-INF/views/sleep.jsp"));
        
        mockMvc.perform(delete("/sleeps/{id}", id))
                .andExpect(status().is3xxRedirection());
        
        mockMvc.perform(get("/sleeps/{id}", id))
                .andExpect(model().attributeDoesNotExist("sleep"));

    }

    private Long getAttributeId(Map<String, Object> model) {
        for (String key : model.keySet()) {
            try {
                Long value = Long.parseLong("" + model.get(key));
                return value;
            } catch (Throwable t) {

            }
        }

        return -1L;
    }
}
