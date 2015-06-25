package wad.controller;

import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wad.domain.Order;
import wad.repository.OrderRepository;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    // yksinkertaistamme sovellusta hieman; tarjolla on vain kolmea tuotetta
    // ja niitä ei haeta tietokannasta
    @ModelAttribute("items")
    public List<String> getItems() {
        return Arrays.asList("Porkkana", "Kaali", "Virtahepo");
    }

    @ModelAttribute("order")
    public Order getOrder() {
        return new Order();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String viewForm() {
        return "/WEB-INF/views/form.jsp";
    }

    @RequestMapping(value = "{orderId}", method = RequestMethod.GET)
    public String viewOrder(Model model, @PathVariable Long orderId) {
        model.addAttribute("order", orderRepository.findOne(orderId));
        return "/WEB-INF/views/order.jsp";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(
            @ModelAttribute("order") Order order,
            BindingResult bindingResult) {

        order = orderRepository.save(order);


        return "redirect:/orders";
    }
}
