package com.example.stockdividend.web;

import com.example.stockdividend.model.ScrapedResult;
import com.example.stockdividend.service.FinanceService;
import lombok.AllArgsConstructor;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finance")
@AllArgsConstructor
public class FinanceContorrler {

    private final FinanceService financeService;

    @GetMapping("/dividend/{companyname}")
    public ResponseEntity<?> searchFinace(@PathVariable("companyname") String companyName){
        var result = this.financeService.getDividendByCompanyName(companyName);
        return ResponseEntity.ok(result);
    }
}
