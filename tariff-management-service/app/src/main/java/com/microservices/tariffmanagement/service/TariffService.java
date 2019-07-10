package com.microservices.tariffmanagement.service;

import com.microservices.tariffmanagement.dto.CreationTariffDto;
import com.microservices.tariffmanagement.entity.TariffEntity;

import java.util.List;

public interface TariffService {
    TariffEntity createTariff(CreationTariffDto tariff);

    List<TariffEntity> getAllTariffs();

    TariffEntity getTariffById(int tariffId);
}
