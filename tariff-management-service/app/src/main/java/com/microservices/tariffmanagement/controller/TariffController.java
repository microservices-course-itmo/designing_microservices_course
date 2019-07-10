package com.microservices.tariffmanagement.controller;

import com.microservices.tariffmanagement.dto.CreationTariffDto;
import com.microservices.tariffmanagement.entity.TariffEntity;
import com.microservices.tariffmanagement.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("tariffs")
public class TariffController {
    private TariffService tariffService;

    @Autowired
    public TariffController(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public long createTariff(@Valid @RequestBody CreationTariffDto tariff) {
        return tariffService.createTariff(tariff).getId();
    }

    @GetMapping
    public List<TariffEntity> getAllTariffs() {
        return tariffService.getAllTariffs();
    }

    @GetMapping(value = "{tariffId}")
    public TariffEntity getTariffById(@PathVariable int tariffId) {
        return tariffService.getTariffById(tariffId);
    }
}
