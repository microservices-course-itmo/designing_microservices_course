package com.microservices.tariffmanagement.controller;

import com.microservices.tariffmanagement.dto.TariffDto;
import com.microservices.tariffmanagement.service.TariffService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tariffs")
public class TariffController {
    private TariffService tariffService;

    public TariffController(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public long createTariff(@RequestBody TariffDto tariff) {
        return tariffService.createTariff(tariff);
    }
}
