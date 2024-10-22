package com.example.cnweb_nhom5.controller.client;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.cnweb_nhom5.domain.Cart;
import com.example.cnweb_nhom5.domain.CartDetail;
import com.example.cnweb_nhom5.domain.Category;
import com.example.cnweb_nhom5.domain.Factory;
import com.example.cnweb_nhom5.domain.Payment;
import com.example.cnweb_nhom5.domain.Product;
import com.example.cnweb_nhom5.domain.Product_;
import com.example.cnweb_nhom5.domain.Review;
import com.example.cnweb_nhom5.domain.Target;
import com.example.cnweb_nhom5.domain.User;
import com.example.cnweb_nhom5.domain.dto.ProductCriteriaDTO;
import com.example.cnweb_nhom5.service.CategoryService;
import com.example.cnweb_nhom5.service.FactoryService;
import com.example.cnweb_nhom5.service.PaymentService;
import com.example.cnweb_nhom5.service.ProductService;
import com.example.cnweb_nhom5.service.ReviewService;
import com.example.cnweb_nhom5.service.TargetService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ItemController {

    private final ProductService productService;
    private final ReviewService reviewService;
    private final PaymentService paymentService;
    private final FactoryService factoryService;
    private final TargetService targetService;
    private final CategoryService categoryService;
    public ItemController(ProductService productService
    ,ReviewService reviewService
    , PaymentService paymentService,
            FactoryService factoryService,
            TargetService targetService,
            CategoryService categoryService) {
        this.productService = productService;
        this.reviewService = reviewService;
        this.paymentService = paymentService;
        this.factoryService = factoryService;
        this.targetService = targetService;
        this.categoryService = categoryService;
    }

    // @GetMapping("/product/{id}")
    // public String getProductPage(Model model, @PathVariable long id) {
    //     Product pr = this.productService.fetchProductById(id).get();
    //     model.addAttribute("product", pr);
    //     model.addAttribute("id", id);
    //     return "client/product/detail";
    // }
    @GetMapping("/product/{id}")
    public String getProductPage(Model model, @PathVariable long id) {
        Product pr = this.productService.fetchProductById(id).get();
        List<Review> reviews = reviewService.getReviewsByProductId(id); // Thay đổi tùy theo cách bạn truy xuất đánh giá

        List<Category> categories = categoryService.fetchAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("product", pr);
        model.addAttribute("id", id);
        model.addAttribute("reviews", reviews); // Thêm reviews vào model

        return "client/product/detail"; // Đảm bảo rằng đây là tên JSP của bạn
    }

    @PostMapping("/add-product-to-cart/{id}")
    public String addProductToCart(@PathVariable long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        long productId = id;
        String email = (String) session.getAttribute("email");

        this.productService.handleAddProductToCart(email, productId, session, 1);

        return "redirect:/";
    }

    @GetMapping("/cart")
    public String getCartPage(Model model, HttpServletRequest request) {
        User currentUser = new User();// null
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        currentUser.setId(id);

        Cart cart = this.productService.fetchByUser(currentUser);

        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();

        double totalPrice = 0;
        for (CartDetail cd : cartDetails) {
            totalPrice += cd.getPrice() * cd.getQuantity();
        }

        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);

        model.addAttribute("cart", cart);

        return "client/cart/show";
    }

    @PostMapping("/delete-cart-product/{id}")
    public String deleteCartDetail(@PathVariable long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        long cartDetailId = id;
        this.productService.handleRemoveCartDetail(cartDetailId, session);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String getCheckOutPage(Model model, HttpServletRequest request) {
        User currentUser = new User();// null
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        currentUser.setId(id);

        Cart cart = this.productService.fetchByUser(currentUser);
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();
        List<Payment> payment = paymentService.fetchAllPayments();
        
        
        double totalPrice = 0;
        for (CartDetail cd : cartDetails) {
            totalPrice += cd.getPrice() * cd.getQuantity();
        }
// add model payments
        model.addAttribute("payment", payment);
        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);

        return "client/cart/checkout";
    }

    // @PostMapping("/confirm-checkout")
    // public String getCheckOutPage(@ModelAttribute("cart") Cart cart, Model model) {
    //     List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();
    //     this.productService.handleUpdateCartBeforeCheckout(cartDetails);
    //     List<Payment> paymentMethods = paymentService.fetchAllPayments();
    //     model.addAttribute("paymentMethods", paymentMethods);
    //     return "redirect:/checkout";
    // }
    
    @PostMapping("/confirm-checkout")
    public String getCheckOutPage(@ModelAttribute("cart") Cart cart, Model model) {
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();
        this.productService.handleUpdateCartBeforeCheckout(cartDetails);
       
        return "redirect:/checkout"; // Trả về tên view để không mất dữ liệu trong model
    }

    // @PostMapping("/place-order")
    // public String handlePlaceOrder(
    //         HttpServletRequest request,
    //         @RequestParam("receiverName") String receiverName,
    //         @RequestParam("receiverAddress") String receiverAddress,
    //         @RequestParam("receiverPhone") String receiverPhone,
    //         @RequestParam double totalPrice) {
    //     User currentUser = new User();// null
    //     HttpSession session = request.getSession(false);
    //     long id = (long) session.getAttribute("id");
    //     currentUser.setId(id);

    //     // Xử lý việc đặt hàng
    //     Cart cart = this.productService.fetchByUser(currentUser);
    //     List<CartDetail> cartDetails = cart.getCartDetails();

    //     // Duyệt qua từng sản phẩm trong giỏ hàng
    //     for (CartDetail cartDetail : cartDetails) {
    //         Product product = cartDetail.getProduct();

    //         // Cập nhật số lượng sold và quantity của sản phẩm
    //         long quantityBought = cartDetail.getQuantity();
    //         product.setSold(product.getSold() + quantityBought);
    //         product.setQuantity(product.getQuantity() - quantityBought);

    //         // Lưu thay đổi vào database
    //         productService.createProduct(product);
    //     }
    //     this.productService.handlePlaceOrder(currentUser, session, receiverName, receiverAddress, receiverPhone);

    //     return "redirect:/thanks";
    // }

    @PostMapping("/place-order")
    public String handlePlaceOrder(
            HttpServletRequest request,
            @RequestParam("receiverName") String receiverName,
            @RequestParam("receiverAddress") String receiverAddress,
            @RequestParam("receiverPhone") String receiverPhone,
            @RequestParam double totalPrice,
            @RequestParam("paymentId") long paymentId) { // Thêm tham số paymentMethod

        User currentUser = new User();
        HttpSession session = request.getSession(false);

        
        // Kiểm tra session và lấy id người dùng
        if (session != null && session.getAttribute("id") != null) {
            long id = (long) session.getAttribute("id");
            currentUser.setId(id);
        } else {
            return "redirect:/login"; // Nếu không có session, chuyển hướng đến trang đăng nhập
        }
        
        // Nếu phương thức thanh toán là VNPay, chuyển đến create_payment
        if (paymentId == 2L) {
            System.err.println(paymentId);
    //         return "redirect:/api/payment/create_payment?totalPrice=" + totalPrice + 
    //    "&receiverName=" + receiverName + 
    //    "&receiverAddress=" + receiverAddress + 
    //    "&receiverPhone=" + receiverPhone +
    //    "&paymentId=" + paymentId;
                return "redirect:/api/payment/create_payment?totalPrice=" + totalPrice +
            "&receiverName=" + URLEncoder.encode(receiverName, StandardCharsets.UTF_8) +
            "&receiverAddress=" + URLEncoder.encode(receiverAddress, StandardCharsets.UTF_8) +
            "&receiverPhone=" + receiverPhone +
            "&paymentId=" + paymentId;
        }

        // Xử lý việc đặt hàng cho phương thức thanh toán COD
        Cart cart = this.productService.fetchByUser(currentUser);
        List<CartDetail> cartDetails = cart.getCartDetails();

        // Duyệt qua từng sản phẩm trong giỏ hàng
        for (CartDetail cartDetail : cartDetails) {
            Product product = cartDetail.getProduct();

            // Cập nhật số lượng sold và quantity của sản phẩm
            long quantityBought = cartDetail.getQuantity();
            product.setSold(product.getSold() + quantityBought);
            product.setQuantity(product.getQuantity() - quantityBought);

            // Lưu thay đổi vào database
            productService.createProduct(product);
        }

        this.productService.handlePlaceOrder(currentUser, session, receiverName, receiverAddress, receiverPhone,paymentId);

        return "redirect:/thanks";
    }

    @GetMapping("/thanks")
    public String getThankYouPage(Model model) {

        return "client/cart/thanks";
    }

    @PostMapping("/add-product-from-view-detail")
    public String handleAddProductFromViewDetail(
            @RequestParam("id") long id,
            @RequestParam("quantity") long quantity,
            HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        String email = (String) session.getAttribute("email");
        this.productService.handleAddProductToCart(email, id, session, quantity);
        return "redirect:/product/" + id;
    }

    @GetMapping("/products")
    public String getProductPage(Model model,
            ProductCriteriaDTO productCriteriaDTO,
            HttpServletRequest request) {
        int page = 1;
        try {
            if (productCriteriaDTO.getPage().isPresent()) {
                // convert from String to int
                page = Integer.parseInt(productCriteriaDTO.getPage().get());
            } else {
                // page = 1
            }
        } catch (Exception e) {
            // page = 1
            // TODO: handle exception
        }

        // check sort price
        Pageable pageable = PageRequest.of(page - 1, 10);

        if (productCriteriaDTO.getSort() != null && productCriteriaDTO.getSort().isPresent()) {
            String sort = productCriteriaDTO.getSort().get();
            if (sort.equals("gia-tang-dan")) {
                pageable = PageRequest.of(page - 1, 10, Sort.by(Product_.PRICE).ascending());
            } else if (sort.equals("gia-giam-dan")) {
                pageable = PageRequest.of(page - 1, 10, Sort.by(Product_.PRICE).descending());
            }
        }

        Page<Product> prs = this.productService.fetchProductsWithSpec(pageable, productCriteriaDTO);

        List<Product> products = prs.getContent().size() > 0 ? prs.getContent()
                : new ArrayList<Product>();

        List<Factory> factories = this.factoryService.fetchAllFactories();

        // Thêm danh sách vào model để hiển thị trên giao diện
        model.addAttribute("factories", factories);

        List<Target> targets = this.targetService.fetchAllTargets();
 
        // Thêm danh sách vào model để hiển thị trên giao diện
        model.addAttribute("targets", targets);

        String qs = request.getQueryString();
        if (qs != null && !qs.isBlank()) {
            // remove page
            qs = qs.replace("page=" + page, "");
        }

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", prs.getTotalPages());
        model.addAttribute("queryString", qs);
        return "client/product/show";
    }

}
