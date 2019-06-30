package com.microservices.ordermanagement.app.impl;

import com.microservices.ordermanagement.app.api.PendingDetailService;
import com.microservices.ordermanagement.app.entity.PendingDetailEntity;
import com.microservices.ordermanagement.app.repository.PendingDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PendingDetailServiceImpl implements PendingDetailService {
    private final Logger logger = LoggerFactory.getLogger(PendingDetailServiceImpl.class);

    private PendingDetailsRepository pendingDetailsRepository;

    @Autowired
    public PendingDetailServiceImpl(PendingDetailsRepository pendingDetailsRepository) {
        this.pendingDetailsRepository = pendingDetailsRepository;
    }

    @Override
    public int registerPendingDetail(int weight) {
        if (weight < 1) { // TODO sukhoa check the top boundary
            throw new IllegalArgumentException("Weight might not be less than 1kg");
        }
        PendingDetailEntity saved = pendingDetailsRepository.save(
                new PendingDetailEntity(weight));

        logger.debug("Created new postponed detail id {}", saved.getId());
        return saved.getId();
    }
}
