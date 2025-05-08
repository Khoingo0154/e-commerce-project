package com.example.demo.controller;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.PromotionsDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.Promotions;
import com.example.demo.entity.User;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import com.example.demo.service.PromotionService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String admin(Model model, @RequestParam("page") Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(0), 3);
        Page<User> users = userService.getAllCustomer(pageable);
        model.addAttribute("users", users);
        model.addAttribute("startPage", 0);
        return "/admin/home-admin";
    }

    @GetMapping("/promo-code")
    public String promoCode(Model model, @RequestParam("page") Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(0), 3);
        Page<Promotions> promotions = promotionService.getAllPromotions(pageable);
        model.addAttribute("promotions", promotions);
        model.addAttribute("startPage", 0);
        return "/admin/promo-code";
    }

    @GetMapping("/category")
    public String category(Model model, @RequestParam("page") Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(0), 3);
        Page<Category> categories = categoryService.findAll(pageable);
        List<Promotions> promotion = promotionService.getAll();

        model.addAttribute("categories", categories);
        model.addAttribute("promotions", promotion);
        model.addAttribute("startPage", 0);
        return "/admin/category";
    }

    @PostMapping("/category")
    public String addCategory(@ModelAttribute CategoryDTO categoryDTO) {
        categoryService.save(categoryDTO);
        return "redirect:/admin/category";
    }
 //phan trang
    @GetMapping("/product")
    public String product(Model model, @RequestParam("page") Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(0), 3);
        Page<Product> products = productService.findAll(pageable);
        List<Category> categories = categoryService.getAll();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("startPage", 0);
        return "/admin/product";
    }

    @PostMapping("/product/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.removeById(id);
        return "redirect:/admin/product";
    }

    @GetMapping("/staff")
    public String staff(Model model, @RequestParam("page") Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(0), 3);
        Page<User> users = userService.findAllUserByOffice(pageable);
        model.addAttribute("users", users);
        model.addAttribute("startPage", 0);
        return "/admin/staff";
    }

    // Add employee
    @PostMapping("/staff/insert")
    public String insertEmployee(@ModelAttribute UserDTO userDTO) {
        userService.saveEmployee(userDTO);
        return "redirect:/admin/staff";
    }

    @GetMapping("/bill-mana")
    public String bills() {
        return "/admin/bills";
    }

    @GetMapping("/revenue")
    public String revenue() {
        return "/admin/revenue";
    }

    @PostMapping("/product")
    public String addProduct(@RequestParam("file")MultipartFile file, @ModelAttribute ProductDTO product) {
        productService.saveProductToDatabase(file, product);
        return "redirect:/admin/product";
    }

    @PostMapping("/promotion")
    public String addPromotion(@ModelAttribute PromotionsDTO promotionsDTO) {
        promotionService.savePromotion(promotionsDTO);
        return "redirect:/admin/promo-code";
    }
}
