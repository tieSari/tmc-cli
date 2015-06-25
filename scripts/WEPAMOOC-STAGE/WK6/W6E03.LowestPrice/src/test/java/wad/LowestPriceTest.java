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
import wad.service.ext.BaseService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
@Points("W6E03")
public class LowestPriceTest {

    @Autowired
    private WebApplicationContext webAppContext;
    
    @Autowired
    private List<BaseService> services;
    
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void noTrickeryInService() throws Exception {
        MockBaseService service = new MockBaseService();
        long time = System.currentTimeMillis();
        service.getLowestPrice("book");
        service.getLowestPrice("desk");
        service.getLowestPrice("coffee");

        assertTrue(System.currentTimeMillis() - time > 1500L);
        
        assertTrue(services.size() == 8);
    }

    @Test
    public void retrievingQuoteShouldBeReasonablyFast() throws Exception {
        long start = System.currentTimeMillis();

        MvcResult res = mockMvc.perform(post("/quotes").param("item", "selkaeraaputin"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        assertTrue(System.currentTimeMillis() - start < 3000L);
        
        for (BaseService service : services) {
            assertTrue(service.getQuotes().size() > 0);
        }
    }
}
