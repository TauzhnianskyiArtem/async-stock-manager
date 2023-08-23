package com.example.asyncstockmanager.repository;

import com.example.asyncstockmanager.entity.Company;
import com.example.asyncstockmanager.entity.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomRepositoryImpl implements CustomRepository {

    private final DatabaseClient databaseClient;

    @Override
    public void saveCompanies(List<Company> companies) {
        companies.forEach(company -> saveOneCompany(company).subscribe());
        log.debug("{} companies were saved", companies.size());
    }


    public void saveStocks(List<Stock> stocks) {
        stocks.stream()
                .filter(Objects::nonNull)
                .forEach(stock -> saveOneStock(stock).subscribe());
        log.debug("{} stocks were saved", stocks.size());
    }


    public Mono<Void> saveOneCompany(Company company) {
        return databaseClient.sql("INSERT INTO company (symbol) VALUES ($1) ON CONFLICT (symbol) DO NOTHING ")
                .bind("$1", company.getSymbol()).then();
    }

    public Mono<Void> saveOneStock(Stock stock) {
        return databaseClient.sql("INSERT INTO stock AS newRow (symbol, latest_price, change, previous_volume, volume, company_name) " +
                        "VALUES ($1, $2, $3, $4, $5, $6) ON CONFLICT (symbol) DO UPDATE SET " +
                        "delta_price = EXCLUDED.latest_price - newRow.latest_price")
                .bind("$1", stock.getSymbol())
                .bind("$2", stock.getLatestPrice())
                .bind("$3", stock.getChange())
                .bind("$4", stock.getPreviousVolume())
                .bind("$5", stock.getVolume())
                .bind("$6", stock.getCompanyName())
                .then();
    }

}