package com.example.asyncstockmanager.service;

import com.example.asyncstockmanager.client.ExApiExchangeClient;
import com.example.asyncstockmanager.client.QueueClient;
import com.example.asyncstockmanager.dto.CompanyDTO;
import com.example.asyncstockmanager.dto.StockDto;
import com.example.asyncstockmanager.entity.Company;
import com.example.asyncstockmanager.entity.Stock;
import com.example.asyncstockmanager.mapper.CompanyMapper;
import com.example.asyncstockmanager.mapper.StockMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Service
@RequiredArgsConstructor
public class DataProcessingService {

    @Value("${service.number-of-companies}")
    private Integer NUMBER_OF_COMPANIES;

    private final ExApiExchangeClient apiClient;
    private final QueueClient queueClient;
    private final CompanyMapper companyMapper;
    private final StockMapper stockMapper;


    public List<Company> getCompaniesData() {
        queueClient.getCompanyQueue().clear();
        return apiClient.getCompanies().stream()
                .filter(CompanyDTO::isEnabled)
                .limit(NUMBER_OF_COMPANIES)
                .map(companyDTO -> {
                    String url = apiClient.getStocksUrl(companyDTO.symbol());
                    queueClient.putToQueue(url);
                    return companyMapper.companyDtoToCompany(companyDTO);
                })
                .toList();
    }

    public List<Stock> getStocksData() {
        List<CompletableFuture<Stock>> futures = queueClient.getCompanyQueue().stream()
                .map(task -> apiClient.getOneCompanyStock(task)
                        .thenApplyAsync(stockMapper::stockDtoToStock))
                .toList();

        return futures.stream().map(CompletableFuture::join).toList();
    }
}
