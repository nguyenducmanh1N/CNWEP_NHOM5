package com.example.cnweb_nhom5.controller.admin;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.cnweb_nhom5.domain.Factory;
import com.example.cnweb_nhom5.service.FactoryService;

import jakarta.validation.Valid;

@Controller
public class FactoryController {

    private final FactoryService factoryService;

    public FactoryController(FactoryService factoryService) {
        this.factoryService = factoryService;
    }

    @GetMapping("/admin/factory")
    public String getFactoryList(Model model) {
        List<Factory> factories = factoryService.fetchAllFactories();
        model.addAttribute("factories", factories);
        return "admin/factory/show";
    }

    @GetMapping("/admin/factory/create")
    public String getCreateFactoryPage(Model model) {
        model.addAttribute("newFactory", new Factory());
        return "admin/factory/create";
    }
