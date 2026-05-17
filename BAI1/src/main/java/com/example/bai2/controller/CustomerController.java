package com.example.bai2.controller;

import com.example.bai2.model.dto.response.ApiDataResponse;
import com.example.bai2.model.entity.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {
    private List<Customer> customers = new ArrayList<>();

    @PostMapping("/add-customer")
    public ResponseEntity<ApiDataResponse<Customer>> addCustomer(@RequestBody Customer customer) {
        customers.add(customer);
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Customer added successfully",
                customer,
                HttpStatus.CREATED
        ), HttpStatus.CREATED);
    }

    @PutMapping("/edit-customer/{id}")
    public ResponseEntity<ApiDataResponse<Customer>> editCustomer(
            @PathVariable Long id,
            @RequestBody Customer updatedCustomer
    ) {
        Optional<Customer> existingCustomer = customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
        if (existingCustomer.isPresent()) {
            Customer customer = existingCustomer.get();
            customer.setName(updatedCustomer.getName());
            customer.setEmail(updatedCustomer.getEmail());
            return new ResponseEntity<>(new ApiDataResponse<>(
                    true,
                    "Customer updated successfully",
                    customer,
                    HttpStatus.OK
            ), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ApiDataResponse<>(), HttpStatus.NOT_FOUND);
    }
}
