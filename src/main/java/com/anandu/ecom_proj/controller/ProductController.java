package com.anandu.ecom_proj.controller;

import com.anandu.ecom_proj.model.Product;
import com.anandu.ecom_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping ("/products")
    public List<Product> getAllProduct(){ 
        return service.getAllProduct();

    }
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id)
    {
        Product product=service.getProductById(id);
        if(product!=null)
            return new ResponseEntity<>(product, HttpStatus.OK);
        else
            return new  ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile){
        try {
            Product product1 = service.addProduct(product, imageFile);
            return new ResponseEntity<>(product1,HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    @GetMapping("/products/{id}/image")
    public ResponseEntity<byte[]> getImagebyProductId(@PathVariable int id){
        Product product=service.getProductById(id);
        byte[] imageFile= product.getImageDate();
        return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType()))
                .body(imageFile);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id,@RequestPart Product product,
                                                @RequestPart MultipartFile imageFile){
        Product product1= null;
        try {
            product1 = service.updateProduct(id,product,imageFile);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
        }
        if(product1!=null)
            return new  ResponseEntity<>("UPDATED ",HttpStatus.OK);
        else
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
    }
    @DeleteMapping("/products/{id}")
    public ResponseEntity<String>deleteById(@PathVariable int id){
        Product product =service.getProductById(id);
        if(product!=null){
            service.deleteProduct(id);
            return new ResponseEntity<>("Updated ",HttpStatus.OK);}
        else
            return new ResponseEntity<>("No product",HttpStatus.NOT_FOUND);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword){
        List<Product> product =service.searchProduct(keyword);
        return new ResponseEntity<>(product,HttpStatus.OK);

    }

}

