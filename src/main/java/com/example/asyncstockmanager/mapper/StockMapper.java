package com.example.asyncstockmanager.mapper;

import com.example.asyncstockmanager.dto.StockDto;
import com.example.asyncstockmanager.entity.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StockMapper {

    Stock INSTANCE = Mappers.getMapper(Stock.class);

    StockDto stockStockDTO(Stock stock);

    Stock stockDtoToStock(StockDto stockDto);

}
