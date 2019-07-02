package com.microservices.laundrymanagement.controllers;

import com.microservices.laundrymanagement.service.LaundryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("laundries")
public class LaundryController {
    private final Logger logger = LoggerFactory.getLogger(LaundryController.class);

    private LaundryService laundryService;

    @Autowired
    public LaundryController(LaundryService laundryService) {
        this.laundryService = laundryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public int registerLaundry(@RequestParam String laundryName) { // TODO sukhoa : return something reasonable
        logger.info("Got request for creating laundry with name \"{}\"", laundryName);
        return laundryService.registerLaundry(laundryName);
    }
}