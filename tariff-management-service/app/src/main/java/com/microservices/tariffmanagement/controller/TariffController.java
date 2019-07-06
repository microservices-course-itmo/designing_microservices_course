package com.microservices.tariffmanagement.controller;

import com.microservices.tariffmanagement.dto.TariffDto;
import com.microservices.tariffmanagement.service.TariffService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tariffs")
public class TariffController {
    private TariffService tariffService;

    public TariffController(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @PostMapping
    public long createTariff(@RequestBody TariffDto tariff) {
        return tariffService.createTariff(tariff);
    }
}
