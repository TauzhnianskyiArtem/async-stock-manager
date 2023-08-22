package com.example.asyncstockmanager.service;

import com.example.asyncstockmanager.BaseAbstractTest;
import com.example.asyncstockmanager.entity.Stock;
import com.example.asyncstockmanager.repository.StockRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import reactor.core.publisher.Flux;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class AnalyticServiceTest extends BaseAbstractTest {
    @SpyBean
    private AnalyticService analyticService;
    @MockBean
    private StockRepository stockRepository;

    @Test
    public void whenContextLoadsSuccessful() {
        assertThat(stockRepository).isNotNull();
    }

    @Test
    void whenGetTopFiveStockPriceIsSuccessful() {
        when(stockRepository.findTop5ExpensiveStocks()).thenReturn(Flux.just(new Stock()));
        analyticService.getTopFiveStockPrice();

        verify(stockRepository, atLeastOnce()).findTop5ExpensiveStocks();
    }

    @Test
    void whenGetTopFiveDeltaStockPriceIsSuccessful() {//        Given
        when(stockRepository.findTop5HighestDeltaPrice()).thenReturn(Flux.just(new Stock()));
        analyticService.getTopFiveDeltaStocksPrice();

        verify(stockRepository, atLeastOnce()).findTop5HighestDeltaPrice();
    }
}