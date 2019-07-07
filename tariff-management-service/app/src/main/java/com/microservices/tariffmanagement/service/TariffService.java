package com.microservices.tariffmanagement.service;

import com.microservices.tariffmanagement.dto.TariffDto;
import com.microservices.tariffmanagement.entity.TariffEntity;

public interface TariffService {
    TariffEntity createTariff(TariffDto tariff);
}
