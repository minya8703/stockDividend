package com.example.stockdividend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finance")
public class FinanceContorrler {
    @GetMapping("/dividend/{companyname}")
    public ResponseEntity<?> searchFinace(@PathVariable String companyName){
        return null;
    }
}
