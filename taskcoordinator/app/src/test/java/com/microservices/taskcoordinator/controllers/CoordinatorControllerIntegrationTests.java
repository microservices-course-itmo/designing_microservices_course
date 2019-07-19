package com.microservices.taskcoordinator.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.taskcoordinator.dto.LaundryStateDto;
import com.microservices.taskcoordinator.dto.OrderDetailDto;
import com.microservices.taskcoordinator.dto.OrderDto;
import com.microservices.taskcoordinator.dto.inbound.InboundLaundryStateDto;
import com.microservices.taskcoordinator.dto.inbound.OrderCoordinationDto;
import com.microservices.taskcoordinator.dto.inbound.OrderProcessedDto;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDto;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDto;
import com.microservices.taskcoordinator.entity.OrderStatus;
import com.microservices.taskcoordinator.service.LaundryStateService;
import com.microservices.taskcoordinator.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
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
    public void testCoordinateOrder_allValid_equalsLoadedLaundries_orderCoordinatedAndLaundryStateChanged() throws Exception {

        LaundryStateDto laundryWillBeChosen = laundryStateService.getLeastLoadedLaundry();

        List<OrderDetailDto> inputOrderDetails = new ArrayList<>(Arrays.asList(
                new OrderDetailDto(10, DEFAULT_DETAIL_WEIGHT, DEFAULT_DETAIL_DURATION, INPUT_ORDER_ID),
                new OrderDetailDto(11, DEFAULT_DETAIL_WEIGHT, DEFAULT_DETAIL_DURATION, INPUT_ORDER_ID)
        ));
        OrderCoordinationDto coordinationDto = new OrderCoordinationDto(INPUT_ORDER_ID, inputOrderDetails);

        String coordinationDtoJson = objectMapper.writeValueAsString(coordinationDto);
        String responseOrderSubmission = mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType(APPLICATION_JSON.toString())
                .content(coordinationDtoJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        OrderSubmissionDto orderSubmissionDto = objectMapper.readValue(responseOrderSubmission, OrderSubmissionDto.class);

        //checkout order and laundryState from DB to see a difference
        OrderDto orderCreated = orderService.getOrderById(INPUT_ORDER_ID);
        LaundryStateDto laundryStateForCreatedOrder = laundryStateService.getLaundryStateById(orderSubmissionDto.getLaundryId());

        assertEquals(coordinationDto.getOrderId(), orderSubmissionDto.getOrderId());
        assertEquals(orderSubmissionDto.getOrderId(), orderCreated.getId());
        assertEquals(OrderStatus.RESERVED, orderCreated.getStatus());

        long reserveDiff = laundryStateForCreatedOrder.getReservedTime() - laundryWillBeChosen.getReservedTime();
        assertEquals((long) orderCreated.getDuration(), reserveDiff);
    }

    @Test
    @Sql(scripts = "/test-data/one_laundry.sql")
    @Transactional
    public void testSubmitOrder_fromReservedStatus() throws Exception {
        int orderId = 1;
        int laundryId = 1;

        InboundLaundryStateDto inboundLaundryStateDto = new InboundLaundryStateDto(laundryId, 200L, 2);
        OrderSubmittedDto orderSubmittedDto = new OrderSubmittedDto(orderId, inboundLaundryStateDto);

        String orderSubmittedJson = objectMapper.writeValueAsString(orderSubmittedDto);
        mockMvc.perform(put("/orders/666/status/submitted")
                .contentType(APPLICATION_JSON.toString())
                .content(orderSubmittedJson))
                .andExpect(status().isOk());

        LaundryStateDto updatedLaundryState = laundryStateService.getLaundryStateById(laundryId);

        assertEquals(200, updatedLaundryState.getQueueWaitingTime().longValue());
        assertEquals(100, updatedLaundryState.getReservedTime().longValue());
        assertEquals(2, updatedLaundryState.getVersion().intValue());
        assertEquals(OrderStatus.SUBMITTED, orderService.getOrderById(orderId).getStatus());
    }

    @Test
    @Sql(scripts = "/test-data/basic.sql")
    @Transactional
    public void testProcessOrder_fromSubmittedStatus() throws Exception {
        int orderId = 3;
        int laundryId = 1;

        InboundLaundryStateDto inboundLaundryStateDto = new InboundLaundryStateDto(laundryId, 200L, 2);
        OrderProcessedDto orderSubmittedDto = new OrderProcessedDto(orderId, inboundLaundryStateDto, 102L);

        String orderSubmittedJson = objectMapper.writeValueAsString(orderSubmittedDto);
        mockMvc.perform(put("/orders/666/status/processed")
                .contentType(APPLICATION_JSON.toString())
                .content(orderSubmittedJson))
                .andExpect(status().isOk());

        LaundryStateDto updatedLaundryState = laundryStateService.getLaundryStateById(laundryId);

        assertEquals(200, updatedLaundryState.getQueueWaitingTime().longValue());
        assertEquals(100, updatedLaundryState.getReservedTime().longValue());
        assertEquals(2, updatedLaundryState.getVersion().intValue());
        assertEquals(OrderStatus.COMPLETE, orderService.getOrderById(orderId).getStatus());
    }

    @Test
    @Sql(scripts = "/test-data/one_laundry.sql")
    @Transactional
    public void testProcessOrder_fromReservedStatus() throws Exception {
        int orderId = 1;
        int laundryId = 1;

        InboundLaundryStateDto inboundLaundryStateDto = new InboundLaundryStateDto(laundryId, 200L, 2);
        OrderProcessedDto orderSubmittedDto = new OrderProcessedDto(orderId, inboundLaundryStateDto, 102L);

        LaundryStateDto initialLaundryState = laundryStateService.getLaundryStateById(laundryId);
        OrderDto initialOrder = orderService.getOrderById(orderId);


        String orderSubmittedJson = objectMapper.writeValueAsString(orderSubmittedDto);
        try {
            Exception mvcResult = mockMvc.perform(put("/orders/666/status/processed")
                    .contentType(APPLICATION_JSON.toString())
                    .content(orderSubmittedJson)).andReturn().getResolvedException();
            if (mvcResult != null) {
                throw mvcResult;
            } else
                fail();
        } catch (Exception e) {
            LaundryStateDto updatedLaundryState = laundryStateService.getLaundryStateById(laundryId);
            OrderDto updatedOrder = orderService.getOrderById(orderId);

            assertEquals(initialLaundryState, updatedLaundryState);
            assertEquals(initialOrder, updatedOrder);
        }

    }

    @Test(expected = MethodArgumentNotValidException.class)
    //is rather a unit test
    public void inputDto_wrongDto() throws Exception {
        InboundLaundryStateDto inboundLaundryStateDto = new InboundLaundryStateDto(1, -100L, 2);
        OrderProcessedDto orderSubmittedDto = new OrderProcessedDto(1, inboundLaundryStateDto, 100L);

        String orderSubmittedJson = objectMapper.writeValueAsString(orderSubmittedDto);
        MvcResult mvcResult = mockMvc.perform(put("/orders/666/status/processed")
                .contentType(APPLICATION_JSON.toString())
                .content(orderSubmittedJson))
                .andExpect(status().is4xxClientError())
                .andReturn();
        throw Objects.requireNonNull(mvcResult.getResolvedException());
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testCoordinateOrder_NoLaundries() {
        laundryStateService.getLeastLoadedLaundry();
    }
}
