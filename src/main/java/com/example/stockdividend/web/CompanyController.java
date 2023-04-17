package com.example.stockdividend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompanyController {

    @GetMapping("/finance/dividend/{companyName")
    public ResponseEntity<?> searchFinance(@PathVariable String companyName){
        return null;
    }
    @GetMapping("/company/autocomplete")
    public ResponseEntity<?> autocomplete (@PathVariable String companyName){
        return null;
    }
    @GetMapping("/company")
    public ResponseEntity<?> searchCompany(){
        return null;
    }

    @PostMapping("/company")
    public ResponseEntity<?> addCompany(){
        return null;
    }

    @DeleteMapping("/company")
    public ResponseEntity<?> deleteCompany(){
        return null;
    }
}
