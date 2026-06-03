package com.example.bai1.controller;

import com.example.bai1.model.dto.response.ApiDataResponse;
import com.example.bai1.model.entity.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
    @GetMapping("/products")
    public ResponseEntity<ApiDataResponse<List<Product>>> products() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("HP001", "Áo thun 'Code is Life'", 199.000));
        products.add(new Product("HP002", "Móc khóa 'Bug Free'", 99.000));
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Lấy danh sách sản phẩm thành công",
                products,
                HttpStatus.OK
        ), HttpStatus.OK);
    }
}
