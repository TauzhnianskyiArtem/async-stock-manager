package com.example.asyncstockmanager.mapper;

import com.example.asyncstockmanager.dto.CompanyDTO;
import com.example.asyncstockmanager.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    Company INSTANCE = Mappers.getMapper(Company.class);

    CompanyDTO companyToCompanyDTO(Company company);

    Company companyDtoToCompany(CompanyDTO company);

}
