package com.microservices.laundrymanagement.service.impl;

import com.microservices.laundrymanagement.entity.LaundryEntity;
import com.microservices.laundrymanagement.entity.LaundryStateEntity;
import com.microservices.laundrymanagement.repository.LaundryRepository;
import com.microservices.laundrymanagement.repository.LaundryStateRepository;
import com.microservices.laundrymanagement.service.LaundryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LaundryServiceImpl implements LaundryService {
    private final Logger logger = LoggerFactory.getLogger(LaundryServiceImpl.class);

    private LaundryStateRepository laundryStateRepository;
    private LaundryRepository laundryRepository;

    @Autowired
    public LaundryServiceImpl(LaundryStateRepository laundryStateRepository,
                              LaundryRepository laundryRepository) {
        this.laundryStateRepository = laundryStateRepository;
        this.laundryRepository = laundryRepository;
    }

    @Override
    @Transactional
    public int registerLaundry(String name) {
        logger.info("Creating queue...");
        if (laundryRepository.existsByName(name)) {
            logger.error("Laundry with name \"{}\" exists", name);
            throw new IllegalArgumentException("Laundry with name " + name + " exists");
        }

        LaundryEntity laundryEntity = new LaundryEntity(name);
        int laundryId = laundryRepository.save(laundryEntity).getId();

        LaundryStateEntity laundryStateEntity = new LaundryStateEntity(laundryId);
        laundryStateRepository.save(laundryStateEntity);

        logger.info("Created queue with id {}", laundryId);
        return laundryId;
    }
}
