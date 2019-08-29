package com.microservices.apigateway.controller;

import com.microservices.ordermanagement.api.dto.AssignTariffDto;
import com.microservices.ordermanagement.api.dto.OrderDto;
import com.microservices.ordermanagement.api.service.OrderServiceApi;
import com.microservices.tariffmanagement.api.dto.TariffDto;
import com.microservices.tariffmanagement.api.service.TariffServiceApi;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ComposingController {

    @Autowired
    private TariffServiceApi tariffServiceApi;

    @Autowired
    private OrderServiceApi orderServiceApi;

    @Autowired
    private ModelMapper mapper;


    @PutMapping(value = "/order/{orderId}/detail/{detailId}/tariff/{tariffId}")
    public OrderDto assignTariffToOrderDetail(
            @PathVariable Integer orderId,
            @PathVariable Integer detailId,
            @PathVariable Integer tariffId) {

        //return new OrderDto();
        TariffDto tariffById = tariffServiceApi.getTariffById(tariffId);
        return orderServiceApi.assignTariffToOrderDetail(
                new AssignTariffDto(
                        orderId,
                        detailId,
                        mapper.map(
                                tariffById,
                                com.microservices.ordermanagement.api.dto.TariffDto.class))
        );
    }

    /*@GetMapping(value = "/tariffs")
    public List<TariffDto> getAllTariffs() {
        return Lists.newArrayList(
                new TariffDto(1, "vfb", new BigDecimal(4), 44));
    }*/

}
