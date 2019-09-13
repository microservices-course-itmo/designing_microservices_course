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
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TariffServiceImplTest {
    private static final double CORRECT_PRICE = 11.0;

    private static final double NEGATIVE_PRICE = -11.0;

    private static final long CORRECT_WASHING_TIME = 12L;

    private static final long NEGATIVE_WASHING_TIME = -12L;

    private static final String UNIQUE_NAME = "unique";

    private static final String NON_UNIQUE_NAME = "non-unique";

    private static final String EXCEPTION_MESSAGE_FOR_NEGATIVE_VALUES = "Tariff not created, price and washing time" +
            " should be a non negative";

    private static final String EXCEPTION_MASSAGE_FOR_NON_UNIQUE_NAME = "Tariff not created, tariff with name " +
            NON_UNIQUE_NAME +
            " already exists";

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

        assertEquals(savedTariffDto.getId(), savedTariffEntity.toTariffDto().getId());
        assertEquals(savedTariffDto.getName(), savedTariffEntity.toTariffDto().getName());
        assertEquals(savedTariffDto.getPrice(), savedTariffEntity.toTariffDto().getPrice());
        assertEquals(savedTariffDto.getWashingTime(), savedTariffEntity.toTariffDto().getWashingTime());
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

        assertEquals(savedTariffDto.getId(), savedTariffEntity.toTariffDto().getId());
        assertEquals(savedTariffDto.getName(), savedTariffEntity.toTariffDto().getName());
        assertEquals(savedTariffDto.getPrice(), savedTariffEntity.toTariffDto().getPrice());
        assertEquals(savedTariffDto.getWashingTime(), savedTariffEntity.toTariffDto().getWashingTime());
    }

    @Test
    public void testTariffService_priceIsNegative() throws IllegalArgumentException {
        CreationTariffDto creationTariffDto = new CreationTariffDto();
        creationTariffDto.setName(UNIQUE_NAME);
        creationTariffDto.setPrice(BigDecimal.valueOf(NEGATIVE_PRICE));
        creationTariffDto.setWashingTime(CORRECT_WASHING_TIME);
        try {
            tariffService.createTariff(creationTariffDto);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException thrown) {
            assertEquals(thrown.getMessage(), EXCEPTION_MESSAGE_FOR_NEGATIVE_VALUES);
        }
    }

    @Test
    public void testTariffService_washingTimeIsNegative() throws IllegalArgumentException {
        CreationTariffDto creationTariffDto = new CreationTariffDto();
        creationTariffDto.setName(UNIQUE_NAME);
        creationTariffDto.setPrice(BigDecimal.valueOf(CORRECT_PRICE));
        creationTariffDto.setWashingTime(NEGATIVE_WASHING_TIME);
        try {
            tariffService.createTariff(creationTariffDto);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException thrown) {
            assertEquals(thrown.getMessage(), EXCEPTION_MESSAGE_FOR_NEGATIVE_VALUES);
        }
    }

    @Test
    public void testTariffService_priceAndWashingTimeIsNegative() throws IllegalArgumentException {
        CreationTariffDto creationTariffDto = new CreationTariffDto();
        creationTariffDto.setName(UNIQUE_NAME);
        creationTariffDto.setPrice(BigDecimal.valueOf(NEGATIVE_PRICE));
        creationTariffDto.setWashingTime(NEGATIVE_WASHING_TIME);
        try {
            tariffService.createTariff(creationTariffDto);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException thrown) {
            assertEquals(thrown.getMessage(), EXCEPTION_MESSAGE_FOR_NEGATIVE_VALUES);
        }
    }

    @Test
    public void testTariffService_nameIsNonUnique() throws IllegalArgumentException {
        CreationTariffDto creationTariffDto = new CreationTariffDto();
        creationTariffDto.setName(NON_UNIQUE_NAME);
        creationTariffDto.setPrice(BigDecimal.valueOf(CORRECT_PRICE));
        creationTariffDto.setWashingTime(CORRECT_WASHING_TIME);

        when(tariffRepository.findByName(NON_UNIQUE_NAME))
                .thenReturn(Optional.of(new TariffEntity(creationTariffDto)));

        try {
            tariffService.createTariff(creationTariffDto);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException thrown) {
            assertEquals(thrown.getMessage(), EXCEPTION_MASSAGE_FOR_NON_UNIQUE_NAME);
        }
    }
}
