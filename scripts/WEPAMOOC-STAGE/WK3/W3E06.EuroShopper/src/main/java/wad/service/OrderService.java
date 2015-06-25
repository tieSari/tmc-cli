package wad.service;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wad.domain.Item;
import wad.domain.Order;
import wad.domain.OrderItem;
import wad.domain.UserDetails;
import wad.repository.ItemRepository;
import wad.repository.OrderRepository;

@Service
public class OrderService {

    
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public void placeOrder(UserDetails userDetails) {

        Order order = new Order();
        order.setUserDetails(userDetails);


        orderRepository.save(order);
    }
}
