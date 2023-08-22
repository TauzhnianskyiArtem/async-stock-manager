package com.example.asyncstockmanager.client;

import com.example.asyncstockmanager.dto.CompanyDTO;
import com.example.asyncstockmanager.dto.StockDto;
import com.example.asyncstockmanager.exception.WrongExecuteException;
import com.example.asyncstockmanager.util.TrackExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExApiExchangeClient {

    @Value("${api.external.ref-data-url}")
    protected String refDataUrl;
    @Value("${api.external.stock-data-url}")
    protected String stockPriceUrl;
    @Value("${api.external.token}")
    protected String token;

    private final RestTemplate restTemplate;
//    private final RestClient restClient;

    public List<CompanyDTO> getCompanies() {
        List<CompanyDTO> companies = new ArrayList<>();
        ParameterizedTypeReference<List<CompanyDTO>> typeRef = new ParameterizedTypeReference<>() {
        };

        List<CompanyDTO> dtos = Optional.of(restTemplate.exchange(String.format(refDataUrl, token), HttpMethod.GET, null, typeRef))
                .filter(response -> (response.getStatusCode().is2xxSuccessful() && Objects.nonNull(response.getBody())))
                .map(HttpEntity::getBody)
                .orElseThrow(RuntimeException::new);

        return dtos;
    }

    @TrackExecutionTime
    public CompletableFuture<StockDto> getOneCompanyStock(String url) {

        return CompletableFuture.supplyAsync(() -> {
            ResponseEntity<StockDto[]>
                    response = restTemplate.exchange(url, HttpMethod.GET, null, StockDto[].class);

            if (response.getStatusCode().is2xxSuccessful() && Objects.nonNull(response.getBody())) {
                log.debug("Response received status {} and number of stock {}", response.getStatusCode(), response.getBody().length);
                return response.getBody()[0];
            } else {
                log.debug("Response received status {}", response.getStatusCode());
                throw new WrongExecuteException("Wrong execute Exception: {}", url);
            }
        });
    }

    public String getStocksUrl(String symbol) {
        return String.format(stockPriceUrl, symbol, token);
    }
}
