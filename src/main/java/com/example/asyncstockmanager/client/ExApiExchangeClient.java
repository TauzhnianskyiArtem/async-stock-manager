package com.ihorshulha.asyncapidatamanager.client;

import com.ihorshulha.asyncapidatamanager.dto.CompanyDTO;
import com.ihorshulha.asyncapidatamanager.dto.StockDto;
import com.ihorshulha.asyncapidatamanager.util.TrackExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.ihorshulha.asyncapidatamanager.util.IgnoreRuntimeException.ignoredException;

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
        ParameterizedTypeReference<List<CompanyDTO>> typeRef = new ParameterizedTypeReference<>() {};

        List<CompanyDTO> dtos = Optional.of(restTemplate.exchange(String.format(refDataUrl, token), HttpMethod.GET, null, typeRef))
                .filter(response -> (response.getStatusCode().is2xxSuccessful() && Objects.nonNull(response.getBody())))
                .map(HttpEntity::getBody)
                .orElseThrow(RuntimeException::new);

        return dtos;
    }

    @TrackExecutionTime
    public Optional<StockDto> getOneCompanyStock(String url) {
        AtomicReference<Optional<StockDto>> result = new AtomicReference<>(Optional.empty());

        ignoredException(() -> {
                    ResponseEntity<StockDto[]> response = restTemplate.exchange(url, HttpMethod.GET, null, StockDto[].class);

                    if (response.getStatusCode().is2xxSuccessful() && Objects.nonNull(response.getBody())) {
                        result.set(Optional.ofNullable(response.getBody()[0]));
                        log.debug("Response received status {} and number of stock {}", response.getStatusCode(), response.getBody().length);
                    } else {
                        log.debug("Response received status {}", response.getStatusCode());
                    }
                }
        );
        return result.get();
    }

    public String getStocksUrl(String symbol) {
        return String.format(stockPriceUrl, symbol, token);
    }
}
