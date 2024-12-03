package com.bilal.ecom_proj.controller;

import com.bilal.ecom_proj.model.Product;
import com.bilal.ecom_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping ("/api")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {

        Product product = service.getProductById(id);

        if (product != null)
            return new ResponseEntity<>(product, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile) {
        try {
            Product product1 = service.addProduct(product, imageFile);
            return new ResponseEntity<>(product1, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId){

        Product product = service.getProductById(productId);
        byte[] imageFile = product.getImageData();

        return ResponseEntity.ok()
                .body(imageFile);
    }
    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct (@PathVariable int id, @RequestPart Product product,
                                           @RequestPart MultipartFile imageFile ){
        Product product1 = null;
        try {
            product1 = service.updateProduct(id, product, imageFile);
        } catch (IOException e) {
            return new ResponseEntity<>("failed to update", HttpStatus.BAD_REQUEST);        }
        if (product1 != null){
            return new ResponseEntity("updated" ,HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("failed to update", HttpStatus.BAD_REQUEST);
    }
    @DeleteMapping ("/product/{id}")
    public ResponseEntity<String> deleteProduct (@PathVariable int id ){
        Product product = service.getProductById(id);
        if (product != null){
            service.deleteProduct(id);
            return new ResponseEntity<> ("Product Deleted" , HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Product not found" , HttpStatus.BAD_REQUEST);
    }

    @GetMapping ("/products/search")
    public ResponseEntity<List<Product>> searchProducts (@RequestParam String keyword){
        List<Product> products = service.searchProducts(keyword);
        System.out.println("searching for: " + keyword);
        return new ResponseEntity<> (products,HttpStatus.OK);
    }
}