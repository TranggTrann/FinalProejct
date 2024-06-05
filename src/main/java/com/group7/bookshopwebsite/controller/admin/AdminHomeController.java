package com.group7.bookshopwebsite.controller.admin;

import com.group7.bookshopwebsite.controller.common.BaseController;
import com.group7.bookshopwebsite.entity.Book;
import com.group7.bookshopwebsite.entity.Category;
import com.group7.bookshopwebsite.entity.Order;
import com.group7.bookshopwebsite.service.BookService;
import com.group7.bookshopwebsite.service.CategoryService;
import com.group7.bookshopwebsite.service.OrderService;
import com.group7.bookshopwebsite.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminHomeController extends BaseController {
    private OrderService orderService;
    private UserService userService;
    private BookService bookService;
    private CategoryService categoryService;

    @GetMapping
    public String getAdminHomePage(Model model){
        List<Order> orders = orderService.getTop10orders();
        model.addAttribute("orders", orders);
        BigDecimal totalRevenue = orderService.getTotalRevenue();

        Long numberOfUsers = userService.countUser();
        Long numberOfBooks = bookService.countBook();
        Long numberOfOrders = orderService.countOrder();

        HashMap<Long, Double> dataSet = new HashMap<>();
        for(Book item : bookService.getTop10BestSeller()){
            dataSet.put(item.getId(), item.getTotalRevenue());
        }

        HashMap<String, Double> dataCategorySet = new HashMap<>();
        for(Category category : categoryService.getAllCategories()){
            double sum = 0;
            for(Book item : category.getBookList()){
                sum += item.getTotalRevenue();
            }
            dataCategorySet.put(category.getName(), sum);
        }

        model.addAttribute("numberOfUsers", numberOfUsers);
        model.addAttribute("numberOfBooks", numberOfBooks);
        model.addAttribute("numberOfOrders", numberOfOrders);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("labels", dataSet.keySet());
        model.addAttribute("data", dataSet.values());
        model.addAttribute("categoryLabels", dataCategorySet.keySet());
        model.addAttribute("categoryData", dataCategorySet.values());
        return "admin/index";
    }
}
