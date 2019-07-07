package com.microservices.tariffmanagement.service;

import com.microservices.tariffmanagement.dto.TariffDto;
import com.microservices.tariffmanagement.entity.TariffEntity;
import com.microservices.tariffmanagement.repository.TariffRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TariffServiceImpl implements TariffService {
    private final Logger logger = LoggerFactory.getLogger(TariffServiceImpl.class);
    private TariffRepository tariffRepository;

    @Autowired
    public TariffServiceImpl(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @Override
    public TariffEntity createTariff(TariffDto tariff) {
        logger.info("Creating tariff: {}...", tariff);
        TariffEntity tariffEntity = new TariffEntity(tariff);
        TariffEntity createdTariff = tariffRepository.save(tariffEntity);
        logger.info("Created new tariff: {}", createdTariff);
        return createdTariff;
    }
}
