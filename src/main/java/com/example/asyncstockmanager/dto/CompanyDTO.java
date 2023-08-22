package com.example.asyncstockmanager.dto;


import org.springframework.data.annotation.Id;

public record CompanyDTO(@Id Integer id, String symbol, Boolean isEnabled) {
}
