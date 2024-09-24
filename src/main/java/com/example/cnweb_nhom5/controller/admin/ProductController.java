package com.example.cnweb_nhom5.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import com.example.cnweb_nhom5.domain.Category;
import com.example.cnweb_nhom5.domain.Product;
import com.example.cnweb_nhom5.service.CategoryService;
import com.example.cnweb_nhom5.service.ProductService;
import com.example.cnweb_nhom5.service.UploadService;

@Controller
public class ProductController {

    private final UploadService uploadService;
    private final ProductService productService;
    private final CategoryService categoryService;

    // Constructor để tiêm các dịch vụ cần thiết
    public ProductController(
            UploadService uploadService,
            ProductService productService,
            CategoryService categoryService) {
        this.uploadService = uploadService;
        this.productService = productService;
        this.categoryService = categoryService;
    }
    
    //Phân trang
     @GetMapping("/admin/product")
    public String getProduct(
            Model model,
            @RequestParam("page") Optional<String> pageOptional) {
        int page = 1; // Mặc định trang là 1
        try {
            if (pageOptional.isPresent()) {
                // Chuyển đổi từ String sang int
                page = Integer.parseInt(pageOptional.get());
            } 
        } catch (Exception e) {
            // Xử lý ngoại lệ nếu không thể chuyển đổi
        }

        // Tạo Pageable cho phân trang
        Pageable pageable = PageRequest.of(page - 1, 5);
        Page<Product> prs = this.productService.fetchProducts(pageable); // Lấy danh sách sản phẩm
        List<Product> listProducts = prs.getContent(); // Lấy nội dung sản phẩm
        model.addAttribute("products", listProducts); // Thêm danh sách sản phẩm vào model
        model.addAttribute("currentPage", page); // Thêm trang hiện tại vào model
        model.addAttribute("totalPages", prs.getTotalPages()); // Thêm tổng số trang vào model

        return "admin/product/show"; // Trả về view hiển thị danh sách sản phẩm
    }
    
    //Tạo sản phẩm mới
    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        List<Category> categories = categoryService.fetchAllCategories();
        
        // Thêm danh sách category vào model để truyền sang view
        model.addAttribute("categories", categories);
        model.addAttribute("newProduct", new Product()); 
        return "admin/product/create"; 
    }
    
    //Xử lý
    @PostMapping("/admin/product/create")
    public String handleCreateProduct(
            @ModelAttribute("newProduct") @Valid Product pr,
            BindingResult newProductBindingResult,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("hoidanitFile") MultipartFile file) {
        if (newProductBindingResult.hasErrors()) {
            return "admin/product/create"; // Quay lại trang tạo nếu có lỗi
        }
        
        //Up load ảnh
        String image = this.uploadService.handleSaveUploadFile(file, "product");
        pr.setImage(image); 
        
        // Tìm Category theo categoryId và gán vào sản phẩm
        Category category = this.categoryService.findById(categoryId);
        pr.setCategory(category);
        
        // Lưu sản phẩm vào database
        this.productService.createProduct(pr);
        return "redirect:/admin/product"; 
    }
    
    //Hiển thị cập nhật sản phẩm
    @GetMapping("/admin/product/update/{id}")
    public String getUpdateProductPage(Model model, @PathVariable long id) {
        Optional<Product> currentProduct = this.productService.fetchProductById(id); 
        List<Category> categories = categoryService.fetchAllCategories(); 
        model.addAttribute("newProduct", currentProduct.get()); 
        model.addAttribute("categories", categories); 
        return "admin/product/update"; 
    }
    
    // Xử lý
    @PostMapping("/admin/product/update")
    public String handleUpdateProduct(@ModelAttribute("newProduct") @Valid Product pr,
            BindingResult newProductBindingResult,
            @RequestParam("categoryId") Long categoryId, 
            @RequestParam("hoidanitFile") MultipartFile file) {
        if (newProductBindingResult.hasErrors()) {
            return "admin/product/update"; 
        }

        Product currentProduct = this.productService.fetchProductById(pr.getId()).get();  //Lấy sản phẩm hiện tại
        if (currentProduct != null) {
            if (!file.isEmpty()) {
                String img = this.uploadService.handleSaveUploadFile(file, "product");
                currentProduct.setImage(img);
            }

            //Cập nhật thông tin sản phảm
            currentProduct.setName(pr.getName());
            currentProduct.setPrice(pr.getPrice());
            currentProduct.setQuantity(pr.getQuantity());
            currentProduct.setDetailDesc(pr.getDetailDesc());
            currentProduct.setShortDesc(pr.getShortDesc());
            currentProduct.setFactory(pr.getFactory());
            currentProduct.setTarget(pr.getTarget());
            
            //Cập nhật category
            Category category = this.categoryService.findById(categoryId);
            currentProduct.setCategory(category);
            
            this.productService.createProduct(currentProduct); 
        }
        return "redirect:/admin/product"; 
    }

    // Xóa sản phẩm
    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id); 
        model.addAttribute("newProduct", new Product()); 
        return "admin/product/delete"; 
    }
    
    // Xử lý
    @PostMapping("/admin/product/delete")
    public String postDeleteProduct(Model model, @ModelAttribute("newProduct") Product pr) {
        this.productService.deleteProduct(pr.getId());
        return "redirect:/admin/product"; 
    }

    //Hieenr thị chi tiết sản phẩm
    @GetMapping("/admin/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id) {
        Product pr = this.productService.fetchProductById(id).get(); 
        model.addAttribute("product", pr); 
        model.addAttribute("id", id); 
        return "admin/product/detail"; 
    }
}
