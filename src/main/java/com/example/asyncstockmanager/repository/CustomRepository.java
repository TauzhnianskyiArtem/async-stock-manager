package com.example.asyncstockmanager.repository;

import com.example.asyncstockmanager.entity.Company;
import com.example.asyncstockmanager.entity.Stock;

import java.util.List;

public interface CustomRepository {

    void saveCompanies(List<Company> companies);

    void saveStocks(List<Stock> stocks);
}
