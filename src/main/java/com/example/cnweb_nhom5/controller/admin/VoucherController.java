package com.example.cnweb_nhom5.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import com.example.cnweb_nhom5.domain.Category;
import com.example.cnweb_nhom5.domain.Product;
import com.example.cnweb_nhom5.domain.Voucher;
import com.example.cnweb_nhom5.service.VoucherService;

import java.util.List;
import java.util.Optional;

@Controller
public class VoucherController {

    private final VoucherService voucherService;

    // Hàm khởi tạo VoucherController và khởi tạo voucherService
    public VoucherController(VoucherService voucherService){
        this.voucherService = voucherService;

    }
    // Phương thức lấy danh sách tất cả các voucher và hiển thị trên trang quản lý voucher
    @GetMapping("/admin/voucher")
    public String getAllVouchers(Model model) {
        List<Voucher> vouchers = voucherService.getAllVouchers();
        model.addAttribute("vouchers", vouchers);
        return "admin/voucher/show";
    }
    // Phương thức hiển thị trang tạo voucher mới
    @GetMapping("admin/voucher/create")
    public String createVoucherPage(Model model) {
        model.addAttribute("newVoucher", new Voucher());
        return "admin/voucher/create";
    }
    // Phương thức xử lý yêu cầu tạo voucher mới
    @PostMapping("admin/voucher/create")
    public String createVoucher(@ModelAttribute Voucher voucher) {
        voucherService.createVoucher(voucher);
        return "redirect:/admin/voucher";
    }
    //1
    // Phương thức hiển thị trang cập nhật voucher
    // @GetMapping("admin/voucher/update/{id}")
    // public String updateVoucherForm(@PathVariable Long id, Model model) {
    //     model.addAttribute("newVoucher", new Voucher());
    //     Voucher voucher = voucherService.getVoucherById(id).orElse(null);
    //     model.addAttribute("voucher", voucher);
    //     return "admin/voucher/update";
    // }

    //2
    @GetMapping("admin/voucher/update/{id}")
    public String getUpdateVoucherPage(Model model, @PathVariable long id) {
        Optional<Voucher> currentVoucher = this.voucherService.getVoucherById(id);
        
        model.addAttribute("newVoucher", currentVoucher.get());
        return "admin/voucher/update";
    }
    
    @PostMapping("/admin/voucher/update")
    public String handleUpdateVoucher(@ModelAttribute("newVoucher") @Valid Voucher vc,
            BindingResult newVoucherBindingResult) {
        if (newVoucherBindingResult.hasErrors()) {
            return "admin/voucher/update";
        }
        Voucher currentVoucher = this.voucherService.getVoucherById(vc.getId()).get();
        if (currentVoucher != null) {
            currentVoucher.setName(vc.getName());
            currentVoucher.setCode(vc.getCode());
            currentVoucher.setDiscountValue(vc.getDiscountValue());
            currentVoucher.setStartDate(vc.getStartDate());
            currentVoucher.setEndDate(vc.getEndDate());
            currentVoucher.setQuantity(vc.getQuantity());
            currentVoucher.setMinimum(vc.getMinimum());
            this.voucherService.createVoucher(currentVoucher);
        }
        return "redirect:/admin/voucher";
    }

    // @PostMapping("/delete/{id}")
    // public String deleteVoucher(@PathVariable Long id) {
    //     voucherService.deleteVoucher(id);
    //     return "redirect:/admin/voucher";
    // }
    @GetMapping("/admin/voucher/delete/{id}")
    public String getDeleteVoucherPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newVoucher", new Voucher());
        return "admin/voucher/delete";
    }
    // Phương thức xử lý xóa voucher
    @PostMapping("/admin/voucher/delete")
    public String postDeleteVoucher(Model model, @ModelAttribute("newVoucher") Voucher vc) {
        this.voucherService.deleteVoucher(vc.getId());
        return "redirect:/admin/voucher";
    }
}
