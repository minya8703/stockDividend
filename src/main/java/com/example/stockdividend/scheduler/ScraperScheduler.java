package com.example.stockdividend.scheduler;

import com.example.stockdividend.model.Company;
import com.example.stockdividend.model.ScrapedResult;
import com.example.stockdividend.model.constants.CacheKey;
import com.example.stockdividend.persist.CompanyRepository;
import com.example.stockdividend.persist.DividendRepository;
import com.example.stockdividend.persist.entity.CompanyEntity;
import com.example.stockdividend.persist.entity.DividendEntity;
import com.example.stockdividend.scraper.Scraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@EnableCaching
@AllArgsConstructor
public class ScraperScheduler {

    private final CompanyRepository companyRepository;

    private final Scraper yahooFinanceScraper;

    private final DividendRepository dividendRepository;

//    @Scheduled(fixedDelay = 1000)
//    public void test1() throws InterruptedException {
//        Thread.sleep(10000);
//        System.out.println(Thread.currentThread().getName()  + " Test 1 : " + LocalDateTime.now());
//    }
//
//    @Scheduled(fixedDelay = 1000)
//    public void test2() throws InterruptedException {
//        Thread.sleep(10000);
//        System.out.println(Thread.currentThread().getName()  + " Test 2 : " + LocalDateTime.now());
//    }

    // 일정 주기마다 수행
    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling(){

        // 저장된 회사 목록을 조회
        List<CompanyEntity> companyList = this.companyRepository.findAll();

        // 회사마다 배당금 정보를 새로 스크래핑
        for(var company : companyList){
            log.info("scraping scheduler is started -> " + company.getName());
            ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(new Company(company.getTicker(), company.getName()));

            // 스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장
           scrapedResult.getDividends().stream()
                   // 디비든 모델을 디비든 엔티티로 매핑
                   .map(e -> new DividendEntity(company.getId(), e))
                   .forEach(e -> {
                       boolean exists = this.dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                       if(!exists) {
                            this.dividendRepository.save(e);
                            log.info("insert new dividend -> " + e.toString());
                       }
                   });
        }


        // 연속적으로 스크래핑 대상 사이트 서버에 요청을 날리 않도록 일시정지
        try{
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}
