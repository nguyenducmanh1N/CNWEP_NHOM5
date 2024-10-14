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

    @PostMapping("/admin/factory/create")
    public String handleCreateFactory(
            @ModelAttribute("newFactory") @Valid Factory factory,
            BindingResult result) {
        if (result.hasErrors()) {
            return "admin/factory/create";
        }

        factoryService.createFactory(factory);
        return "redirect:/admin/factory";
    }

    @GetMapping("/admin/factory/update/{id}")
    public String getUpdateFactoryPage(Model model, @PathVariable long id) {
        Optional<Factory> factory = factoryService.fetchFactoryById(id);
        if (factory.isPresent()) {
            model.addAttribute("newFactory", factory.get());
            return "admin/factory/update";
        }
        return "redirect:/admin/factory";
    }

    @PostMapping("/admin/factory/update")
    public String handleUpdateFactory(
            @ModelAttribute("newFactory") @Valid Factory factory,
            BindingResult result) {
        if (result.hasErrors()) {
            return "admin/factory/update";
        }

        factoryService.createFactory(factory);
        return "redirect:/admin/factory";
    }

    @GetMapping("/admin/factory/delete/{id}")
    public String deleteFactory(@PathVariable long id) {
        factoryService.deleteFactory(id);
        return "redirect:/admin/factory";
    }
}
