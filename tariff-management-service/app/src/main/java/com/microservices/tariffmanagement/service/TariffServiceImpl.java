package com.microservices.tariffmanagement.service;

import com.microservices.tariffmanagement.dto.CreationTariffDto;
import com.microservices.tariffmanagement.entity.TariffEntity;
import com.microservices.tariffmanagement.repository.TariffRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TariffServiceImpl implements TariffService {
    private final Logger logger = LoggerFactory.getLogger(TariffServiceImpl.class);
    private TariffRepository tariffRepository;

    @Autowired
    public TariffServiceImpl(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @Override
    public TariffEntity createTariff(CreationTariffDto tariff) {
        logger.info("Creating tariff: {}...", tariff);
        TariffEntity tariffEntity = new TariffEntity(tariff);
        TariffEntity createdTariff = tariffRepository.save(tariffEntity);
        logger.info("Created new tariff: {}", createdTariff);
        return createdTariff;
    }

    @Override
    public List<TariffEntity> getAllTariffs() {
        List<TariffEntity> allTariffs = new ArrayList<>();
        tariffRepository.findAll().forEach(allTariffs::add);
        logger.info("Found all tariffs {}", allTariffs);
        return allTariffs;
    }

    @Override
    public TariffEntity getTariffById(int tariffId) {
        TariffEntity tariffEntity = tariffRepository.findById(tariffId)
                .orElseThrow(() ->
                        new IllegalArgumentException("No tariff with id " + tariffId + " found"));
        logger.info("Found tariff by id {} - {}", tariffId, tariffEntity);
        return tariffEntity;
    }
}
