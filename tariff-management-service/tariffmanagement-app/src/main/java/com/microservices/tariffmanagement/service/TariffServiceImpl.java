package com.microservices.tariffmanagement.service;

import com.microservices.tariffmanagement.api.dto.TariffDto;
import com.microservices.tariffmanagement.dto.CreationTariffDto;
import com.microservices.tariffmanagement.entity.TariffEntity;
import com.microservices.tariffmanagement.repository.TariffRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

        if (tariffRepository.findByName(tariff.getName()).isPresent()) {
            throw new IllegalArgumentException("Tariff not created, tariff with name " + tariff.getName() + " already exists");
        }

        if (tariff.getPrice().compareTo(BigDecimal.ZERO) < 0 || tariff.getWashingTime() < 0) {
            throw new IllegalArgumentException("Tariff not created, price and washing time should be a non negative");
        }

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

    @Override
    public void deleteTariffById(int tariffId) {
        TariffEntity foundTariff = tariffRepository.findById(tariffId)
                .orElseThrow(() ->
                        new IllegalArgumentException("No tariff with id " + tariffId + " deleted, because not found"));

        logger.info("Deleting tariff with id: {}...", tariffId);
        tariffRepository.deleteById(tariffId);
        TariffDto deletedTariff = foundTariff.toTariffDto();
        logger.info("Deleted tariff: {}...", deletedTariff);
    }
}
