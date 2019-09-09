package com.microservices.tariffmanagement.controller;

import com.microservices.tariffmanagement.api.dto.TariffDto;
import com.microservices.tariffmanagement.dto.CreationTariffDto;
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
    public TariffDto createTariff(@Valid @RequestBody CreationTariffDto tariff) {
        return tariffService.createTariff(tariff);
    }

    @GetMapping
    public List<TariffDto> getAllTariffs() {
        return tariffService.getAllTariffs();
    }

    @GetMapping(value = "{tariffId}")
    public TariffDto getTariffById(@PathVariable int tariffId) {
        return tariffService.getTariffById(tariffId);
    }

    @DeleteMapping(value = "{tariffId}")
    public void deleteTariffById(@PathVariable int tariffId) {
        tariffService.deleteTariffById(tariffId);
    }
}
