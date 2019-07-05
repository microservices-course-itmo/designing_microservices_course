package com.microservices.tariffmanagement.service;

import com.microservices.tariffmanagement.dto.TariffDto;

public interface TariffService {
    long createTariff(TariffDto tariff);
}
