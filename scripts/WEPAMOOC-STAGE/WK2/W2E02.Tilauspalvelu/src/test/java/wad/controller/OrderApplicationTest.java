package wad.controller;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.List;
import java.util.UUID;
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
import wad.domain.Order;
import wad.repository.OrderRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class OrderApplicationTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private OrderRepository orderRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    @Points("W2E02.1")
    public void formReturnedOnGetToOrder() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/form.jsp"));
    }

    @Test
    @Points("W2E02.1")
    public void redirectToSuccessOnSuccessfulPostToOrder() throws Exception {

        String name = UUID.randomUUID().toString().substring(0, 10);
        String address = UUID.randomUUID().toString().substring(0, 16);

        MvcResult result = mockMvc.perform(post("/orders")
                .param("name", name)
                .param("address", address)
                .param("items", "The Firm"))
                .andExpect(status().is3xxRedirection()).andReturn();
    }

    @Test
    @Points("W2E02.1")
    public void returnToFormOnTooShortName() throws Exception {

        String name = UUID.randomUUID().toString().substring(0, 3);
        String address = UUID.randomUUID().toString().substring(0, 16);

        try {
            mockMvc.perform(post("/orders")
                    .param("name", name)
                    .param("address", address)
                    .param("items", "The Firm"))
                    .andExpect(status().isOk())
                    .andExpect(forwardedUrl("/WEB-INF/views/form.jsp"));
        } catch (Throwable t) {
            fail("When the name is too short, the user should be shown the form again with existing inputs and error messages. Error: " + t.getMessage());
        }
    }

    @Test
    @Points("W2E02.1")
    public void returnToFormOnTooLongName() throws Exception {

        String name = UUID.randomUUID().toString() + UUID.randomUUID().toString() + UUID.randomUUID().toString();
        name = name.substring(0, 31);
        String address = UUID.randomUUID().toString().substring(0, 16);

        try {
            mockMvc.perform(post("/orders")
                    .param("name", name)
                    .param("address", address)
                    .param("items", "The Firm"))
                    .andExpect(status().isOk())
                    .andExpect(forwardedUrl("/WEB-INF/views/form.jsp"));
        } catch (Throwable t) {
            fail("When the name is too long, the user should be shown the form again with existing inputs and error messages. Error: " + t.getMessage());
        }
    }

    @Test
    @Points("W2E02.1")
    public void returnToFormOnTooShortAddress() throws Exception {
        String name = UUID.randomUUID().toString().substring(0, 6);
        String address = UUID.randomUUID().toString().substring(0, 3);

        try {
            mockMvc.perform(post("/orders")
                    .param("name", name)
                    .param("address", address)
                    .param("items", "The Firm"))
                    .andExpect(status().isOk())
                    .andExpect(forwardedUrl("/WEB-INF/views/form.jsp"));
        } catch (Throwable t) {
            fail("When the address is too short, the user should be shown the form again with existing inputs and error messages. Error: " + t.getMessage());
        }

    }

    @Test
    @Points("W2E02.1")
    public void returnToFormOnTooLongAddress() throws Exception {
        String name = UUID.randomUUID().toString().substring(0, 6);
        String address = UUID.randomUUID().toString() + UUID.randomUUID().toString() + UUID.randomUUID().toString();
        address = address.substring(0, 51);

        try {
            mockMvc.perform(post("/orders")
                    .param("name", name)
                    .param("address", address)
                    .param("items", "The Firm"))
                    .andExpect(status().isOk())
                    .andExpect(forwardedUrl("/WEB-INF/views/form.jsp"));
        } catch (Throwable t) {
            fail("When the address is too long, the user should be shown the form again with existing inputs and error messages. Error: " + t.getMessage());
        }
    }

    @Test
    @Points("W2E02.1")
    public void returnToFormWhenNoItems() throws Exception {

        String name = UUID.randomUUID().toString().substring(0, 6);
        String address = UUID.randomUUID().toString().substring(0, 6);

        try {
            mockMvc.perform(post("/orders")
                    .param("name", name)
                    .param("address", address))
                    .andExpect(status().isOk())
                    .andExpect(forwardedUrl("/WEB-INF/views/form.jsp"));
        } catch (Throwable t) {
            fail("When no items are selected, the user should be shown the form again with existing inputs and error messages. Error: " + t.getMessage());
        }
    }

    @Test
    @Points("W2E02.2")
    public void hasFlashAttributeMessageOnSuccess() throws Exception {
        orderRepository.deleteAll(); // clear orders

        String name = UUID.randomUUID().toString().substring(0, 6);
        String address = UUID.randomUUID().toString().substring(0, 6);

        MvcResult result = mockMvc.perform(post("/orders")
                .param("name", name)
                .param("address", address)
                .param("items", "The Firm"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(1))
                .andExpect(flash().attribute("message", "Order placed!"))
                .andReturn();

        List<Order> orders = orderRepository.findAll();
        assertTrue("After a single order has been submitted, exactly 1 order should be added to the service.", orders.size() == 1);
    }

    @Test
    @Points("W2E02.2")
    public void hasModelAttributeOrderIdOnSuccess() throws Exception {
        orderRepository.deleteAll(); // clear orders

        String name = UUID.randomUUID().toString().substring(0, 6);
        String address = UUID.randomUUID().toString().substring(0, 6);

        MvcResult result = mockMvc
                .perform(post("/orders")
                        .param("name", name)
                        .param("address", address)
                        .param("items", "The Firm"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeExists("orderId"))
                .andReturn();

        List<Order> orders = orderRepository.findAll();
        assertTrue("After a single order has been submitted, exactly 1 order should be added to the service.", orders.size() == 1);
        String viewname = result.getModelAndView().getViewName();
        assertTrue("The user should be redirected to a new page after an order is successfully placed.", viewname.startsWith("redirect"));
        assertTrue("The returned redirectUrl should contain a string {orderId} for orderId-attribute.", viewname.contains("{orderId}"));
    }

    @Test
    @Points("W2E02.2")
    public void finallyOrderDetailsAreShown() throws Exception {
        orderRepository.deleteAll(); // clear orders

        String name = UUID.randomUUID().toString().substring(0, 6);
        String address = UUID.randomUUID().toString().substring(0, 6);

        MvcResult result = mockMvc
                .perform(post("/orders")
                        .param("name", name)
                        .param("address", address)
                        .param("items", "The Firm"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeExists("orderId"))
                .andReturn();

        List<Order> orders = orderRepository.findAll();
        assertTrue("After a single order has been submitted, exactly 1 order should be added to the service.", orders.size() == 1);
        
        String viewname = result.getModelAndView().getViewName();
        assertTrue("The user should be redirected to a new page after an order is successfully placed. was: " + viewname, viewname.startsWith("redirect"));
        assertTrue("The returned redirectUrl should contain a string {orderId} for orderId-attribute. was: " + viewname, viewname.contains("{orderId}"));

    }
}
