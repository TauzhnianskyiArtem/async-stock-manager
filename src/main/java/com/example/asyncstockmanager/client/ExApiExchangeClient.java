package com.example.asyncstockmanager.client;

import com.example.asyncstockmanager.dto.CompanyDTO;
import com.example.asyncstockmanager.dto.StockDto;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.asyncstockmanager.util.IgnoreRuntimeException.ignoredException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExApiExchangeClient {

    @Value("${api.external.ref-data-url}")
    private String refDataUrl;
    @Value("${api.external.stock-data-url}")
    private String stockPriceUrl;
    @Value("${api.external.token}")
    private String token;

    private final RestTemplate restTemplate;

    public List<CompanyDTO> getCompanies() {
        ParameterizedTypeReference<List<CompanyDTO>> typeRef = new ParameterizedTypeReference<>() {};

        List<CompanyDTO> companyDTOS = Optional.of(restTemplate.exchange(String.format(refDataUrl, token), HttpMethod.GET, null, typeRef))
                .filter(response -> (response.getStatusCode().is2xxSuccessful() && Objects.nonNull(response.getBody())))
                .map(HttpEntity::getBody)
                .orElseThrow(RuntimeException::new);
        log.debug("List companies was received, size of list {}", companyDTOS.size());
        return companyDTOS;
    }

    @TrackExecutionTime
    public StockDto getOneCompanyStock(String url) {
        return Optional.of(getExtResponse(url))
                .filter(response -> (response.getStatusCode().is2xxSuccessful() && Objects.nonNull(response.getBody())))
                .map(response -> response.getBody()[0])
                .orElseThrow(RuntimeException::new);
    }

    private ResponseEntity<StockDto[]> getExtResponse(String url) {
        AtomicReference<ResponseEntity<StockDto[]>> response = new AtomicReference<>();
        ignoredException(() -> response.set(restTemplate.getForEntity(url, StockDto[].class)));
        return response.get();
    }

    public String getStocksUrl(String symbol) {
        String url = String.format(stockPriceUrl, symbol, token);
        log.debug("Url {} was generated.", url);
        return url;
    }
}
