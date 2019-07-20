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

    private static final Integer DEFAULT_LAUNDRY_ID = 1;

    private static final Integer RESERVED_ORDER_ID = 1;

    private static final Integer SUBMITTED_ORDER_ID = 3;

    private static final Long DEFAULT_QUEUE_WAITING_TIME = 200L;

    private static final Long DEFAULT_COMPLETION_TIME = 102L;

    private static final Long DEFAULT_RESERVED_TIME = 100L;

    private static final Integer DEFAULT_LAUNDRY_STATE_VERSION = 2;


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
    public void testCoordinateOrder_allValidAndEquallyLoadedLaundries_orderCoordinatedAndLaundryStateChanged() throws Exception {

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
    public void testSubmitOrder_orderIsReserved_orderIsSubmitted() throws Exception {
        InboundLaundryStateDto inboundLaundryStateDto = new InboundLaundryStateDto(DEFAULT_LAUNDRY_ID,
                DEFAULT_QUEUE_WAITING_TIME, DEFAULT_LAUNDRY_STATE_VERSION);
        OrderSubmittedDto orderSubmittedDto = new OrderSubmittedDto(RESERVED_ORDER_ID, inboundLaundryStateDto);

        String orderSubmittedJson = objectMapper.writeValueAsString(orderSubmittedDto);
        mockMvc.perform(put("/orders/666/status/submitted")
                .contentType(APPLICATION_JSON.toString())
                .content(orderSubmittedJson))
                .andExpect(status().isOk());

        LaundryStateDto updatedLaundryState = laundryStateService.getLaundryStateById(DEFAULT_LAUNDRY_ID);

        assertEquals(DEFAULT_QUEUE_WAITING_TIME, updatedLaundryState.getQueueWaitingTime());
        assertEquals(DEFAULT_RESERVED_TIME, updatedLaundryState.getReservedTime());
        assertEquals(DEFAULT_LAUNDRY_STATE_VERSION, updatedLaundryState.getVersion());
        assertEquals(OrderStatus.SUBMITTED, orderService.getOrderById(RESERVED_ORDER_ID).getStatus());
    }

    @Test
    @Sql(scripts = "/test-data/basic.sql")
    @Transactional
    public void testProcessOrder_orderIsSubmitted_orderIsProcessed() throws Exception {
        InboundLaundryStateDto inboundLaundryStateDto = new InboundLaundryStateDto(DEFAULT_LAUNDRY_ID,
                DEFAULT_QUEUE_WAITING_TIME, DEFAULT_LAUNDRY_STATE_VERSION);
        OrderProcessedDto orderSubmittedDto = new OrderProcessedDto(SUBMITTED_ORDER_ID, inboundLaundryStateDto, DEFAULT_COMPLETION_TIME);

        String orderSubmittedJson = objectMapper.writeValueAsString(orderSubmittedDto);
        mockMvc.perform(put("/orders/666/status/processed")
                .contentType(APPLICATION_JSON.toString())
                .content(orderSubmittedJson))
                .andExpect(status().isOk());

        LaundryStateDto updatedLaundryState = laundryStateService.getLaundryStateById(DEFAULT_LAUNDRY_ID);

        assertEquals(DEFAULT_QUEUE_WAITING_TIME, updatedLaundryState.getQueueWaitingTime());
        assertEquals(DEFAULT_RESERVED_TIME, updatedLaundryState.getReservedTime());
        assertEquals(DEFAULT_LAUNDRY_STATE_VERSION, updatedLaundryState.getVersion());
        assertEquals(OrderStatus.COMPLETE, orderService.getOrderById(SUBMITTED_ORDER_ID).getStatus());
    }

    @Test
    @Sql(scripts = "/test-data/one_laundry.sql")
    @Transactional
    public void testProcessOrder_orderIsReserved_orderIsNotProcessedDbIsNotChanged() throws Exception {
        InboundLaundryStateDto inboundLaundryStateDto = new InboundLaundryStateDto(DEFAULT_LAUNDRY_ID, 200L, 2);
        OrderProcessedDto orderSubmittedDto = new OrderProcessedDto(RESERVED_ORDER_ID, inboundLaundryStateDto, 102L);

        LaundryStateDto initialLaundryState = laundryStateService.getLaundryStateById(DEFAULT_LAUNDRY_ID);
        OrderDto initialOrder = orderService.getOrderById(RESERVED_ORDER_ID);

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
            LaundryStateDto updatedLaundryState = laundryStateService.getLaundryStateById(DEFAULT_LAUNDRY_ID);
            OrderDto updatedOrder = orderService.getOrderById(RESERVED_ORDER_ID);

            assertEquals(initialLaundryState, updatedLaundryState);
            assertEquals(initialOrder, updatedOrder);
        }

    }

    @Test(expected = MethodArgumentNotValidException.class)
    //is rather a unit test
    public void testProcessOrder_invalidInputDto_orderValidationExceptionIsThrown() throws Exception {
        InboundLaundryStateDto inboundLaundryStateDto = new InboundLaundryStateDto(DEFAULT_LAUNDRY_ID, -100L, 2);
        OrderProcessedDto orderSubmittedDto = new OrderProcessedDto(SUBMITTED_ORDER_ID, inboundLaundryStateDto, 100L);

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
    public void testCoordinateOrder_noLaundries_exceptionIsThrown() {
        laundryStateService.getLeastLoadedLaundry();
    }
}
