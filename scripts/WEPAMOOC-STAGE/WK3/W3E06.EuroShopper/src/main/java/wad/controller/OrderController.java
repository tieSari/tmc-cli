package wad.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wad.domain.UserDetails;
import wad.service.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ModelAttribute("userDetails")
    private UserDetails getUserDetails() {
        return new UserDetails();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String view(Model model) {
        model.addAttribute("orders", orderService.list());
        return "/WEB-INF/views/order.jsp";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String placeorder(RedirectAttributes redirectAttributes,
            @Valid @ModelAttribute("userDetails") UserDetails userDetails,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/WEB-INF/views/order.jsp";
        }

        orderService.placeOrder(userDetails);

        redirectAttributes.addFlashAttribute("message", "Order placed!");
        return "redirect:/orders";
    }
}
