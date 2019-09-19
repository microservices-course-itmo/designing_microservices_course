package com.microservices.tariffmanagement.service;

import com.microservices.tariffmanagement.api.dto.TariffDto;
import com.microservices.tariffmanagement.dto.CreationTariffDto;
import com.microservices.tariffmanagement.entity.TariffEntity;
import com.microservices.tariffmanagement.repository.TariffRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TariffServiceImplTest {
    private static final double CORRECT_PRICE = 11.0;

    private static final double NEGATIVE_PRICE = -11.0;

    private static final long CORRECT_WASHING_TIME = 12L;

    private static final long NEGATIVE_WASHING_TIME = -12L;

    private static final String UNIQUE_NAME = "unique";

    private static final String NON_UNIQUE_NAME = "non-unique";

    @Mock
    private TariffRepository tariffRepository;

    @Spy
    @InjectMocks
    private TariffServiceImpl tariffService;

    @Test
    public void testTariffService_priceAndWashingTimeIsCorrect() {
        CreationTariffDto creationTariffDto = new CreationTariffDto();
        creationTariffDto.setName(UNIQUE_NAME);
        creationTariffDto.setPrice(BigDecimal.valueOf(CORRECT_PRICE));
        creationTariffDto.setWashingTime(CORRECT_WASHING_TIME);

        TariffEntity savedTariffEntity = new TariffEntity(creationTariffDto);

        when(tariffRepository.save(Mockito.any(TariffEntity.class))).thenReturn(savedTariffEntity);

        TariffDto savedTariffDto = tariffService.createTariff(creationTariffDto);

        assertEquals(savedTariffDto.getName(), creationTariffDto.getName());
        assertEquals(savedTariffDto.getPrice(), creationTariffDto.getPrice());
        assertEquals(savedTariffDto.getWashingTime(), (long) creationTariffDto.getWashingTime());
    }

    @Test
    public void testTariffService_priceAndWashingTimeIsZero() {
        CreationTariffDto creationTariffDto = new CreationTariffDto();
        creationTariffDto.setName(UNIQUE_NAME);
        creationTariffDto.setPrice(BigDecimal.valueOf(0.0));
        creationTariffDto.setWashingTime(0L);

        TariffEntity savedTariffEntity = new TariffEntity(creationTariffDto);

        when(tariffRepository.save(Mockito.any(TariffEntity.class))).thenReturn(savedTariffEntity);

        TariffDto savedTariffDto = tariffService.createTariff(creationTariffDto);

        assertEquals(savedTariffDto.getName(), creationTariffDto.getName());
        assertEquals(savedTariffDto.getPrice(), creationTariffDto.getPrice());
        assertEquals(savedTariffDto.getWashingTime(), (long) creationTariffDto.getWashingTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTariffService_priceIsNegative() {
        CreationTariffDto creationTariffDto = new CreationTariffDto();
        creationTariffDto.setName(UNIQUE_NAME);
        creationTariffDto.setPrice(BigDecimal.valueOf(NEGATIVE_PRICE));
        creationTariffDto.setWashingTime(CORRECT_WASHING_TIME);
        tariffService.createTariff(creationTariffDto);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTariffService_washingTimeIsNegative() {
        CreationTariffDto creationTariffDto = new CreationTariffDto();
        creationTariffDto.setName(UNIQUE_NAME);
        creationTariffDto.setPrice(BigDecimal.valueOf(CORRECT_PRICE));
        creationTariffDto.setWashingTime(NEGATIVE_WASHING_TIME);
        tariffService.createTariff(creationTariffDto);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTariffService_priceAndWashingTimeIsNegative() {
        CreationTariffDto creationTariffDto = new CreationTariffDto();
        creationTariffDto.setName(UNIQUE_NAME);
        creationTariffDto.setPrice(BigDecimal.valueOf(NEGATIVE_PRICE));
        creationTariffDto.setWashingTime(NEGATIVE_WASHING_TIME);
        tariffService.createTariff(creationTariffDto);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTariffService_nameIsNonUnique() {
        CreationTariffDto creationTariffDto = new CreationTariffDto();
        creationTariffDto.setName(NON_UNIQUE_NAME);
        creationTariffDto.setPrice(BigDecimal.valueOf(CORRECT_PRICE));
        creationTariffDto.setWashingTime(CORRECT_WASHING_TIME);

        when(tariffRepository.findByName(NON_UNIQUE_NAME))
                .thenReturn(Optional.of(new TariffEntity(creationTariffDto)));

        tariffService.createTariff(creationTariffDto);
    }
}
