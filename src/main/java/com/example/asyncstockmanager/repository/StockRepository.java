package com.example.asyncstockmanager.repository;


import com.example.asyncstockmanager.entity.Stock;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface StockRepository extends R2dbcRepository<Stock, Long> {

    @Query("SELECT DISTINCT symbol, latest_price, company_name FROM stock ORDER BY latest_price DESC, company_name ASC LIMIT 5;")
    Flux<Stock> findTop5ExpensiveStocks();

    @Query("SELECT DISTINCT symbol, latest_price, delta_price, company_name FROM stock ORDER BY delta_price DESC, company_name ASC LIMIT 5;")
    Flux<Stock> findTop5HighestDeltaPrice();

}
