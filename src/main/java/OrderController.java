package vn.hoidanit.laptopshop.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderStatus;
import vn.hoidanit.laptopshop.domain.Voucher;
import vn.hoidanit.laptopshop.service.OrderService;
import vn.hoidanit.laptopshop.service.OrderStatusService;
import vn.hoidanit.laptopshop.service.VoucherService;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final OrderStatusService orderStatusService ;
    private final VoucherService voucherService;

    public OrderController(OrderService orderService, OrderStatusService orderStatusService,
            VoucherService voucherService) {
        this.orderService = orderService;
        this.orderStatusService = orderStatusService;
        this.voucherService = voucherService;
    }

    @GetMapping("/admin/order")
    public String getDashboard(Model model,
            @RequestParam("page") Optional<String> pageOptional) {

        int page = 1;
        try {
            if (pageOptional.isPresent()) {
                // convert from String to int
                page = Integer.parseInt(pageOptional.get());
            } else {
                // page = 1
            }
        } catch (Exception e) {
            // page = 1
            // TODO: handle exception
        }

        Pageable pageable = PageRequest.of(page - 1, 1);
        Page<Order> ordersPage = this.orderService.fetchAllOrders(pageable);
        List<Order> orders = ordersPage.getContent();

        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        return "admin/order/show";
    }

    @GetMapping("/admin/order/{id}")
    public String getOrderDetailPage(Model model, @PathVariable long id) {
        Order order = this.orderService.fetchOrderById(id).get();
        model.addAttribute("order", order);
        model.addAttribute("id", id);
        model.addAttribute("orderDetails", order.getOrderDetails());
        return "admin/order/detail";
    }

    @GetMapping("/admin/order/delete/{id}")
    public String getDeleteOrderPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newOrder", new Order());
        return "admin/order/delete";
    }

    @PostMapping("/admin/order/delete")
    public String postDeleteOrder(@ModelAttribute("newOrder") Order order) {
        this.orderService.deleteOrderById(order.getId());
        return "redirect:/admin/order";
    }

    @GetMapping("/admin/order/update/{id}")
    public String getUpdateOrderPage(Model model, @PathVariable long id
           // ,@RequestParam("categoryId") Long categoryId
            ) {
        Optional<Order> currentOrder = this.orderService.fetchOrderById(id);
        List<OrderStatus> statuses = this.orderStatusService.fetchAllOrderStatuses();
        
        model.addAttribute("newOrder", currentOrder.get());
        model.addAttribute("statuses", statuses);
        return "admin/order/update";
    }

    @PostMapping("/admin/order/update")
    public String handleUpdateOrder(@ModelAttribute("newOrder") Order order
    ) {
        this.orderService.updateOrder(order);
        return "redirect:/admin/order";
    }
    // @PostMapping("/admin/order/update")
    // public String handleUpdateOrder(@ModelAttribute("newOrder") Order order) {
    //     // Kiểm tra nếu có voucher
    //     if (order.getVoucher() != null) {
    //         // Lấy giá trị chiết khấu từ voucher
    //         double discountValue = order.getVoucher().getDiscountValue();

    //         // Tính toán lại tổng số tiền phải trả sau khi áp dụng voucher
    //         double totalPayable = order.getTotalPrice() * (1 - discountValue / 100);

    //         // Cập nhật lại giá trị totalPayable
    //         order.setTotalPayable(totalPayable);
    //     } else {
    //         // Nếu không có voucher, totalPayable bằng totalPrice
    //         order.setTotalPayable(order.getTotalPrice());
    //     }
    //     // Cập nhật thông tin order
    //     this.orderService.updateOrder(order);
    //     return "redirect:/admin/order";
    // }
    

}
