package com.example.asyncstockmanager.repository;

import com.example.asyncstockmanager.entity.Company;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CompanyRepository extends R2dbcRepository<Company, Integer> {
}
