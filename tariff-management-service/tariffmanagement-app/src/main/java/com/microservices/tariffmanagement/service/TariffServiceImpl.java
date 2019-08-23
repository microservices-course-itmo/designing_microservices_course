package com.microservices.tariffmanagement.service;

import com.microservices.tariffmanagement.api.dto.TariffDto;
import com.microservices.tariffmanagement.dto.CreationTariffDto;
import com.microservices.tariffmanagement.entity.TariffEntity;
import com.microservices.tariffmanagement.repository.TariffRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TariffServiceImpl implements TariffService {

    private static final Logger logger = LoggerFactory.getLogger(TariffServiceImpl.class);

    private TariffRepository tariffRepository;

    @Autowired
    public TariffServiceImpl(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @Override
    public TariffDto createTariff(CreationTariffDto tariff) {
        Objects.requireNonNull(tariff);

        logger.info("Creating tariff: {}...", tariff);
        TariffEntity savedTariff = tariffRepository.save(new TariffEntity(tariff));
        TariffDto createdTariff = savedTariff.toTariffDto();
        logger.info("Created new tariff: {}", createdTariff);
        return createdTariff;
    }

    @Override
    public List<TariffDto> getAllTariffs() {
        List<TariffDto> allTariffs = new ArrayList<>();
        tariffRepository.findAll().forEach(tariffEntity ->
                allTariffs.add(tariffEntity.toTariffDto()));
        return allTariffs;
    }

    @Override
    public TariffDto getTariffById(int tariffId) {
        TariffEntity tariffEntity = tariffRepository.findById(tariffId)
                .orElseThrow(() ->
                        new IllegalArgumentException("No tariff with id " + tariffId + " found"));
        return tariffEntity.toTariffDto();
    }
}
