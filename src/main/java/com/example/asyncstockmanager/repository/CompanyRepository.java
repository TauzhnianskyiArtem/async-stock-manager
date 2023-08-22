package com.example.asyncstockmanager.repository;

import com.example.asyncstockmanager.entity.Company;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends R2dbcRepository<Company, Integer> {

//    @Query("SELECT DISTINCT symbol FROM company;")
//    Flux<String> findAllSymbols();
}
