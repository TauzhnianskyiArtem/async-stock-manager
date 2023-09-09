package com.example.asyncstockmanager.client;

import com.example.asyncstockmanager.dto.CompanyDTO;
import com.example.asyncstockmanager.dto.StockDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

public interface ExApiExchangeClient {
    Flux<CompanyDTO> callToCompanyApi();

    Mono<StockDto> callToStockApi(String uri);

    URI getCompanyUri();

    String getStockUri(String symbol);
}
