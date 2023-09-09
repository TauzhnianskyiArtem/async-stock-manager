package com.example.asyncstockmanager.repository;

import com.example.asyncstockmanager.entity.Company;
import com.example.asyncstockmanager.entity.Stock;
import reactor.core.publisher.Mono;

public interface CustomRepository {

    Mono<Company> save(Company company);

    Mono<Stock> saveStock(Stock stock);
}
