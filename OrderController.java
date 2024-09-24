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
