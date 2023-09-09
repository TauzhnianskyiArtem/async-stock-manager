package com.example.asyncstockmanager;

import com.example.asyncstockmanager.client.ExApiExchangeClientImpl;
import com.example.asyncstockmanager.mapper.CompanyMapper;
import com.example.asyncstockmanager.mapper.StockMapper;
import com.example.asyncstockmanager.repository.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;


@SpringBootTest(properties = "scheduling.enabled=false")
@TestConfiguration
public abstract class BaseAbstractTest {

    @MockBean
    public ExApiExchangeClientImpl apiClient;
    @Autowired
    public CompanyMapper companyMapper;
    @Autowired
    public StockMapper stockMapper;
    @MockBean
    public CustomRepository customRepository;
}
