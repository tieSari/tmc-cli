package wad;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.junit.Assert;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wad.domain.Item;
import wad.domain.Order;
import wad.domain.OrderItem;
import wad.repository.ItemRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import wad.repository.OrderRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Points("W3E06.3")
public class C_OrderServiceTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private MockHttpSession mockSession;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void itemsThatHaveBeenAddedToCartAreOrdered() throws Exception {
        Item item = new Item();
        String itemName = "Item: " + UUID.randomUUID().toString().substring(0, 6);
        item.setName(itemName);
        item.setPrice(10 * Math.random());

        item = itemRepository.save(item);

        Long itemCount = new Long(2 + new Random().nextInt(3));
        for (int i = 0; i < itemCount; i++) {
            mockMvc.perform(post("/cart/items/{id}", item.getId()).session(mockSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));    
        }
        
        String name = UUID.randomUUID().toString().substring(0, 6);
        String address = UUID.randomUUID().toString().substring(0, 6);

        mockMvc.perform(
                post("/orders").param("name", name).param("address", address).session(mockSession)
        ).andExpect(status().is3xxRedirection());

        
        boolean orderFound = false;
        for (Order order : orderRepository.findAll()) {
            if (order.getUserDetails() == null) {
                continue;
            }

            if (order.getOrderItems() == null) {
                continue;
            }

            if (!name.equals(order.getUserDetails().getName())) {
                continue;
            }

            if (!address.equals(order.getUserDetails().getAddress())) {
                continue;
            }
            
            // nimi ja osoite ok; tsekataan onko tavarat
            
            for (OrderItem orderItem : order.getOrderItems()) {
                if(orderItem.getItem() == null) {
                    continue;
                }
                
                if(orderItem.getItem().getId() == null) {
                    continue;
                }
                
                if(!item.getId().equals(orderItem.getItem().getId())) {
                    continue;
                }
                
                if(!itemName.equals(orderItem.getItem().getName())) {
                    continue;
                }
                
                
                if(!itemCount.equals(orderItem.getItemCount())) {
                    continue;
                }
                
                // yay!
                orderFound = true;
            }
        }
        
        assertTrue("When making a POST-request to /orders, the items in the shopping cart should be ordered.", orderFound);
        
        
        MvcResult res = mockMvc.perform(get("/cart").session(mockSession))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("items")).andReturn();

        Map<Item, Long> counts = (Map) res.getModelAndView().getModel().get("items");
        assertTrue("When the order has been done, the cart should be empty.", counts.isEmpty());
    }
    
    
    @Test
    public void itemsThatHaveBeenAddedToCartMustNotBeOrderedIfNotSameUser() throws Exception {
        Item item = new Item();
        String itemName = "Item: " + UUID.randomUUID().toString().substring(0, 6);
        item.setName(itemName);
        item.setPrice(10 * Math.random());

        item = itemRepository.save(item);

        Long itemCount = new Long(2 + new Random().nextInt(3));
        for (int i = 0; i < itemCount; i++) {
            mockMvc.perform(post("/cart/items/{id}", item.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));    
        }
        
        String name = UUID.randomUUID().toString().substring(0, 6);
        String address = UUID.randomUUID().toString().substring(0, 6);

        mockMvc.perform(post("/orders").param("name", name).param("address", address)).andExpect(status().is3xxRedirection());

        boolean orderFound = false;
        for (Order order : orderRepository.findAll()) {
            if (order.getUserDetails() == null) {
                continue;
            }

            if (order.getOrderItems() == null) {
                continue;
            }

            if (!name.equals(order.getUserDetails().getName())) {
                continue;
            }

            if (!address.equals(order.getUserDetails().getAddress())) {
                continue;
            }
            
            // nimi ja osoite ok; tsekataan onko tavarat
            
            for (OrderItem orderItem : order.getOrderItems()) {
                if(orderItem.getItem() == null) {
                    continue;
                }
                
                if(orderItem.getItem().getId() == null) {
                    continue;
                }
                
                if(!item.getId().equals(orderItem.getItem().getId())) {
                    continue;
                }
                
                if(!itemName.equals(orderItem.getItem().getName())) {
                    continue;
                }
                
                
                if(!itemCount.equals(orderItem.getItemCount())) {
                    continue;
                }
                
                // yay!
                orderFound = true;
            }
        }
        
        assertFalse("When making a POST-request to /orders, the items in a shopping cart must not be ordered if the user is different.", orderFound);
    }

}
