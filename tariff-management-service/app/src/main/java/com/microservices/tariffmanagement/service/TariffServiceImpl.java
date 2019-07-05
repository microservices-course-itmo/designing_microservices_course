package com.microservices.tariffmanagement.service;

import com.microservices.tariffmanagement.dto.TariffDto;
import com.microservices.tariffmanagement.entity.TariffEntity;
import com.microservices.tariffmanagement.repository.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TariffServiceImpl implements TariffService {
    private TariffRepository tariffRepository;

    @Autowired
    public TariffServiceImpl(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @Override
    public long createTariff(TariffDto tariff) {
        TariffEntity tariffEntity = new TariffEntity(tariff);
        return tariffRepository.save(tariffEntity).getId();
    }
}
