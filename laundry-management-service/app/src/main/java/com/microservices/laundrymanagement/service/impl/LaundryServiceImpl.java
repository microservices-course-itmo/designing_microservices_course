package com.microservices.laundrymanagement.service.impl;

import com.microservices.laundrymanagement.entity.LaundryStateEntity;
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

    @Autowired
    public LaundryServiceImpl(LaundryStateRepository laundryStateRepository) {
        this.laundryStateRepository = laundryStateRepository;
    }

    @Override
    @Transactional
    public int registerLaundry(String name) {
        logger.info("Creating queue with name \"{}\"...", name);
        if (laundryStateRepository.existsByName(name)) {
            logger.error("Laundry with name \"{}\" exists", name);
            throw new IllegalArgumentException("Laundry with name " + name + " exists");
        }

        LaundryStateEntity laundryStateEntity = new LaundryStateEntity(name);
        int laundryId = laundryStateRepository.save(laundryStateEntity).getId();

        logger.info("Created queue with id {} and name \"{}\"", laundryId, name);
        return laundryId;
    }
}
