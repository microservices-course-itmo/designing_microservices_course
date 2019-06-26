package com.microservices.laundrymanagement.service.impl;

import com.microservices.laundrymanagement.entity.LaundryStateEntity;
import com.microservices.laundrymanagement.repository.LaundryStateRepository;
import com.microservices.laundrymanagement.service.LaundryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LaundryServiceImpl implements LaundryService {
    private final Logger logger = LoggerFactory.getLogger(LaundryServiceImpl.class);

    private LaundryStateRepository laundryStateRepository;

    private final Logger logger = LoggerFactory.getLogger(LaundryServiceImpl.class);

    @Autowired
    public LaundryServiceImpl(LaundryStateRepository laundryStateRepository) {
        this.laundryStateRepository = laundryStateRepository;
    }

    @Override
    public int addNewLaundry() {
        logger.info("Creating queue...");

        LaundryStateEntity laundryStateEntity = new LaundryStateEntity();
        int id = laundryStateRepository.save(laundryStateEntity).getId();

        logger.info("Created queue with id {}", id);
        return id;
    }
}
