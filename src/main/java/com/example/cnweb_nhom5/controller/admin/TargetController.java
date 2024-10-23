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

import com.example.cnweb_nhom5.domain.Target;
import com.example.cnweb_nhom5.service.TargetService;

import jakarta.validation.Valid;

@Controller
public class TargetController {

    private final TargetService targetService;

    public TargetController(TargetService targetService) {
        this.targetService = targetService;
    }

    @GetMapping("/admin/target")
    public String getTargetList(Model model) {
        List<Target> targets = targetService.fetchAllTargets();
        model.addAttribute("targets", targets);
        return "admin/target/show";
    }

    @GetMapping("/admin/target/create")
    public String getCreateTargetPage(Model model) {
        model.addAttribute("newTarget", new Target());
        return "admin/target/create";
    }

    @PostMapping("/admin/target/create")
    public String handleCreateTarget(
            @ModelAttribute("newTarget") @Valid Target target,
            BindingResult result) {
        if (result.hasErrors()) {
            return "admin/target/create";
        }

        targetService.createTarget(target);
        return "redirect:/admin/target";
    }

    @GetMapping("/admin/target/update/{id}")
    public String getUpdateTargetPage(Model model, @PathVariable long id) {
        Optional<Target> target = targetService.fetchTargetById(id);
        if (target.isPresent()) {
            model.addAttribute("newTarget", target.get());
            return "admin/target/update";
        }
        return "redirect:/admin/target";
    }

    @PostMapping("/admin/target/update")
    public String handleUpdateTarget(
            @ModelAttribute("newTarget") @Valid Target target,
            BindingResult result) {
        if (result.hasErrors()) {
            return "admin/target/update";
        }

        targetService.createTarget(target);
        return "redirect:/admin/target";
    }

    @GetMapping("/admin/target/delete/{id}")
    public String deleteTarget(@PathVariable long id) {
        targetService.deleteTarget(id);
        return "redirect:/admin/target";
    }
}
