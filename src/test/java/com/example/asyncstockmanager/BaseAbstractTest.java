package com.example.asyncstockmanager;

import com.example.asyncstockmanager.client.QueueClient;
import com.example.asyncstockmanager.mapper.CompanyMapper;
import com.example.asyncstockmanager.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;


@SpringBootTest(properties = "scheduling.enabled=false")
@TestConfiguration
public abstract class BaseAbstractTest {

    @Autowired
    public CompanyMapper companyMapper;

    @Autowired
    public StockMapper stockMapper;

    @SpyBean
    public QueueClient queueClient;
}
