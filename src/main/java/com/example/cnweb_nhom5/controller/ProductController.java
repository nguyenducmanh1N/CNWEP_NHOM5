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

import com.example.cnweb_nhom5.domain.Category;
import com.example.cnweb_nhom5.domain.Factory;
import com.example.cnweb_nhom5.domain.Product;
import com.example.cnweb_nhom5.domain.Target;
import com.example.cnweb_nhom5.service.CategoryService;
import com.example.cnweb_nhom5.service.FactoryService;
import com.example.cnweb_nhom5.service.ProductService;
import com.example.cnweb_nhom5.service.TargetService;
import com.example.cnweb_nhom5.service.UploadService;

import jakarta.validation.Valid;

@Controller
public class ProductController {

    private final UploadService uploadService;
    private final ProductService productService;
    private final FactoryService factoryService;


    private final CategoryService categoryService;
    private final TargetService targetService;

    public ProductController(
            UploadService uploadService,
            ProductService productService,
            CategoryService categoryService,
            FactoryService factoryService,
            TargetService targetService
    ) {
        this.uploadService = uploadService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.factoryService = factoryService;
        this.targetService = targetService;
    }

    @GetMapping("/admin/product")
    public String getProduct(
            Model model,
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

        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<Product> prs = this.productService.fetchProducts(pageable);
        List<Product> listProducts = prs.getContent();
        model.addAttribute("products", listProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", prs.getTotalPages());

        return "admin/product/show";
    }

    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        List<Category> categories = categoryService.fetchAllCategories();
        List<Factory> factories = factoryService.fetchAllFactories();
        List<Target> targets = targetService.fetchAllTargets();

        // Thêm danh sách category vào model để truyền sang view
        model.addAttribute("categories", categories);
        model.addAttribute("factories", factories);
        model.addAttribute("targets", targets);
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String handleCreateProduct(
            @ModelAttribute("newProduct") @Valid Product pr,
            BindingResult newProductBindingResult,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("flowershopFile") MultipartFile file) {
        // validate
        if (newProductBindingResult.hasErrors()) {
            return "admin/product/create";
        }

        // upload image
        String image = this.uploadService.handleSaveUploadFile(file, "product");
        pr.setImage(image);


        // Gán category vào product
        //pr.setCategory(this.categoryService.getCategorybyName(pr.getCategory().getName()));

        Category category = this.categoryService.findById(categoryId); // Tìm Category theo categoryId
        pr.setCategory(category);
        // Lưu sản phẩm vào database

        this.productService.createProduct(pr);
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/update/{id}")
    public String getUpdateProductPage(Model model, @PathVariable long id) {
        Optional<Product> currentProduct = this.productService.fetchProductById(id);
        List<Category> categories = categoryService.fetchAllCategories();
        model.addAttribute("newProduct", currentProduct.get());
        model.addAttribute("categories", categories);
        return "admin/product/update";
    }

    @PostMapping("/admin/product/update")
    public String handleUpdateProduct(@ModelAttribute("newProduct") @Valid Product pr,
                                      BindingResult newProductBindingResult,
                                      @RequestParam("categoryId") Long categoryId, // Nhận categoryId từ form
                                      @RequestParam("flowershopFile") MultipartFile file) {

        // validate
        if (newProductBindingResult.hasErrors()) {
            return "admin/product/update";
        }

        Product currentProduct = this.productService.fetchProductById(pr.getId()).get();
        if (currentProduct != null) {
            // update new image
            if (!file.isEmpty()) {
                String img = this.uploadService.handleSaveUploadFile(file, "product");
                currentProduct.setImage(img);
            }

            currentProduct.setName(pr.getName());
            currentProduct.setPrice(pr.getPrice());
            currentProduct.setQuantity(pr.getQuantity());
            currentProduct.setDetailDesc(pr.getDetailDesc());
            currentProduct.setShortDesc(pr.getShortDesc());
            currentProduct.setFactory(pr.getFactory());
            currentProduct.setTarget(pr.getTarget());
            //currentProduct.setCategory(pr.getCategory());
            // Cập nhật category
            Category category = this.categoryService.findById(categoryId);
            currentProduct.setCategory(category);

            this.productService.createProduct(currentProduct);
        }

        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newProduct", new Product());
        return "admin/product/delete";
    }

    @PostMapping("/admin/product/delete")
    public String postDeleteProduct(Model model, @ModelAttribute("newProduct") Product pr) {
        this.productService.deleteProduct(pr.getId());
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id) {
        Product pr = this.productService.fetchProductById(id).get();
        model.addAttribute("product", pr);
        model.addAttribute("id", id);
        return "admin/product/detail";
    }
}
