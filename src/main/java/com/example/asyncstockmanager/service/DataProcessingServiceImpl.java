package com.example.asyncstockmanager.service;


import com.example.asyncstockmanager.client.ExApiExchangeClientImpl;
import com.example.asyncstockmanager.entity.Company;
import com.example.asyncstockmanager.dto.CompanyDTO;
import com.example.asyncstockmanager.mapper.CompanyMapper;
import com.example.asyncstockmanager.mapper.StockMapper;
import com.example.asyncstockmanager.repository.CustomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@Service
@RequiredArgsConstructor
public class DataProcessingServiceImpl implements DataProcessingService {

    @Value("${service.number-of-companies}")
    private Integer NUMBER_OF_COMPANIES;
    private final List<String> tasks = new CopyOnWriteArrayList<>();
    private AtomicInteger index = new AtomicInteger();

    private final ExApiExchangeClientImpl apiClient;
    private final CompanyMapper companyMapper;
    private final StockMapper stockMapper;
    private final CustomRepository customRepository;

    public Mono<Void> processingCompanyData() {
        tasks.clear();
        return apiClient.callToCompanyApi()
                .onErrorContinue((error, obj) -> log.error("error:[{}]", error.getLocalizedMessage()))
                .filter(CompanyDTO::isEnabled)
                .take(NUMBER_OF_COMPANIES)
                .map(companyMapper::companyDtoToCompany)
                .map(this::addTask)
                .map(customRepository::save)
                .map(Mono::subscribe)
                .then();
    }

    public Mono<Void> processingStockData() {
        return Flux.fromIterable(tasks)
                .flatMap(s -> apiClient.callToStockApi(getTask()))
                .onErrorContinue((error, obj) -> log.error("error:[{}]", error.getLocalizedMessage()))
                .map(stockMapper::stockDtoToStock)
                .map(customRepository::saveStock)
                .map(Mono::subscribe)
                .then();
    }

    private Company addTask(Company company) {
        String uri = apiClient.getStockUri(company.getSymbol());
        tasks.add(uri);
        return company;
    }

    private String getTask() {
        String task = tasks.get(index.getAndIncrement());
        if (index.get() == NUMBER_OF_COMPANIES) index = new AtomicInteger(0);
        return task;
    }
}
