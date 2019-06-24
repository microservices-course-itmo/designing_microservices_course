package com.microservices.laundrymanagement.service.impl;

import com.microservices.laundrymanagement.entity.LaundryStateEntity;
import com.microservices.laundrymanagement.repository.LaundryStateRepository;
import com.microservices.laundrymanagement.service.LaundryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LaundryServiceImpl implements LaundryService {
    private LaundryStateRepository laundryStateRepository;

    @Autowired
    public LaundryServiceImpl(LaundryStateRepository laundryStateRepository) {
        this.laundryStateRepository = laundryStateRepository;
    }

    @Override
    public int addNewLaundry() {
        LaundryStateEntity laundryStateEntity = new LaundryStateEntity();
        LaundryStateEntity id = laundryStateRepository.save(laundryStateEntity);
        return id.getId();
    }
}
