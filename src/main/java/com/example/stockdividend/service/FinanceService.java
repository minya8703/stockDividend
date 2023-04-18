package com.example.stockdividend.service;

import com.example.stockdividend.exception.impl.NoCompanyException;
import com.example.stockdividend.model.Company;
import com.example.stockdividend.model.Dividend;
import com.example.stockdividend.model.ScrapedResult;
import com.example.stockdividend.model.constants.CacheKey;
import com.example.stockdividend.persist.CompanyRepository;
import com.example.stockdividend.persist.DividendRepository;
import com.example.stockdividend.persist.entity.CompanyEntity;
import com.example.stockdividend.persist.entity.DividendEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.collections4.Trie;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    // 요청이 자주 들어오는가?
    // 자주 변경되는 데이터인가?
    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)// 캐쉬에 데이터가 있을 경우 아래의 소스 실행 하지 않는다.
    public ScrapedResult getDividendByCompanyName(String companyName){
        log.info("search company - >" + companyName);
        // 1. 회사명을 기준으로 회자 정보를 조회
        CompanyEntity company = this.companyRepository.findByName(companyName)
                .orElseThrow(() -> new NoCompanyException());
        // 2. 조회된 회사 ID로 배당금 정보 조회
        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(company.getId());
        // 3. 결과
        List<Dividend> dividends = new ArrayList<>();
        for (var entity : dividendEntities){
            dividends.add(new Dividend(entity.getDate(), entity.getDividend()));
        }
        return new ScrapedResult(new Company(company.getTicker(), company.getName())
                , dividends);
    }



}
