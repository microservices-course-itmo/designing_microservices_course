package com.microservices.ordermanagement.app.controller;

import com.microservices.ordermanagement.app.api.PendingDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("details")
public class PendingDetailsController {

    private PendingDetailService pendingDetailService;

    @Autowired
    public PendingDetailsController(PendingDetailService pendingDetailService) {
        this.pendingDetailService = pendingDetailService;
    }

    @PostMapping
    int registerDetail(@RequestParam int weight) {
        return pendingDetailService.registerPendingDetail(weight);
    }
}
