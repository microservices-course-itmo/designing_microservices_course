package com.microservices.tariffmanagement.api.service;

import com.microservices.tariffmanagement.api.dto.TariffDto;

import java.util.List;

public interface TariffService {
    List<TariffDto> getTariffs();

    TariffDto getTariffById(int id);
}
