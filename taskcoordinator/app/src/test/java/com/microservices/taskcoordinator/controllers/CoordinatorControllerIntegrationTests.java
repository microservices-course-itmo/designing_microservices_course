package com.microservices.taskcoordinator.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.taskcoordinator.dto.LaundryStateDto;
import com.microservices.taskcoordinator.dto.OrderDetailDto;
import com.microservices.taskcoordinator.dto.OrderDto;
import com.microservices.taskcoordinator.dto.inbound.OrderCoordinationDto;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDto;
import com.microservices.taskcoordinator.entity.OrderStatus;
import com.microservices.taskcoordinator.repository.LaundryStateRepository;
import com.microservices.taskcoordinator.repository.OrderRepository;
import com.microservices.taskcoordinator.service.LaundryStateService;
import com.microservices.taskcoordinator.service.OrderService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application.properties")
public class CoordinatorControllerIntegrationTests {

    private static final Long DEFAULT_DETAIL_DURATION = 50L;

    private static final Integer INPUT_ORDER_ID = 100;

    private static final Integer DEFAULT_DETAIL_WEIGHT = 20;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LaundryStateService laundryStateService;

    @Autowired
    private OrderService orderService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Sql(scripts = "/test-data/basic.sql")
    @Transactional
    public void t() throws Exception {

        LaundryStateDto laundryWillBeChosen = laundryStateService.getLeastLoadedLaundry();

        List<OrderDetailDto> inputOrderDetails = new ArrayList<>(Arrays.asList(
                new OrderDetailDto(10, DEFAULT_DETAIL_WEIGHT, DEFAULT_DETAIL_DURATION, INPUT_ORDER_ID),
                new OrderDetailDto(11, DEFAULT_DETAIL_WEIGHT, DEFAULT_DETAIL_DURATION, INPUT_ORDER_ID)
        ));
        OrderCoordinationDto coordinationDto = new OrderCoordinationDto(INPUT_ORDER_ID, inputOrderDetails);

        String inputRoleDtoJson = objectMapper.writeValueAsString(coordinationDto);
        String responseOrderSubmission = mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType(APPLICATION_JSON.toString())
                .content(inputRoleDtoJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        OrderSubmissionDto orderSubmissionDto = objectMapper.readValue(responseOrderSubmission, OrderSubmissionDto.class);

        //checkout order and laundryState form DB to see a difference
        OrderDto orderCreated = orderService.getOrderById(INPUT_ORDER_ID);
        LaundryStateDto laundryStateForCreatedOrder = laundryStateService.getLaundryStateById(orderSubmissionDto.getLaundryId());

        Assert.assertEquals(coordinationDto.getOrderId(), orderSubmissionDto.getOrderId());
        Assert.assertEquals(orderSubmissionDto.getOrderId(), orderCreated.getId());
        Assert.assertEquals(OrderStatus.RESERVED, orderCreated.getStatus());

        long reserveDiff = laundryStateForCreatedOrder.getReservedTime() - laundryWillBeChosen.getReservedTime();
        Assert.assertEquals((long) orderCreated.getDuration(), reserveDiff);
    }

}
