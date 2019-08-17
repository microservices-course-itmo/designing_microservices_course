package com.microservices.taskcoordinator.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.taskcoordinator.dto.LaundryStateDto;
import com.microservices.taskcoordinator.dto.OrderDetailDto;
import com.microservices.taskcoordinator.dto.OrderDto;
import com.microservices.taskcoordinator.dto.inbound.InboundLaundryStateDto;
import com.microservices.taskcoordinator.dto.inbound.OrderCompletedDto;
import com.microservices.taskcoordinator.dto.inbound.OrderCoordinationDto;
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
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class CoordinatorControllerIntegrationTests {

    private static final Long DEFAULT_DETAIL_DURATION = 50L;

    private static final Integer DEFAULT_DETAIL_WEIGHT = 20;

    private static final Long DEFAULT_COMPLETION_TIME = 102L;

    private static final Integer EXISTING_LAUNDRY_ID = 1;

    private static final Integer EXISTING_RESERVED_ORDER_ID = 1;

    private static final Integer EXISTING_SUBMITTED_ORDER_ID = 3;

    private static final Integer INBOUND_ORDER_ID = 100;

    private static final Long INBOUND_LAUNDRY_WAITING_TIME = 200L;

    private static final Integer INBOUND_LAUNDRY_STATE_VERSION = 2;

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
                new OrderDetailDto(10, DEFAULT_DETAIL_WEIGHT, DEFAULT_DETAIL_DURATION, INBOUND_ORDER_ID),
                new OrderDetailDto(11, DEFAULT_DETAIL_WEIGHT, DEFAULT_DETAIL_DURATION, INBOUND_ORDER_ID)
        ));
        OrderCoordinationDto coordinationDto = new OrderCoordinationDto(INBOUND_ORDER_ID, inputOrderDetails);

        String coordinationDtoJson = objectMapper.writeValueAsString(coordinationDto);
        String responseOrderSubmission = mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType(APPLICATION_JSON.toString())
                .content(coordinationDtoJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        OrderSubmissionDto orderSubmissionDto = objectMapper.readValue(responseOrderSubmission, OrderSubmissionDto.class);

        //checkout order and laundryState from DB to see a difference
        OrderDto orderCreated = orderService.getOrderById(INBOUND_ORDER_ID);
        LaundryStateDto laundryStateForCreatedOrder = laundryStateService.getLaundryStateById(orderSubmissionDto.getLaundryId());

        assertEquals(coordinationDto.getOrderId(), orderSubmissionDto.getOrderId());
        assertEquals(orderSubmissionDto.getOrderId(), orderCreated.getId());
        assertEquals(OrderStatus.RESERVED, orderCreated.getStatus());

        long reserveDiff = laundryStateForCreatedOrder.getReservedTime() - laundryWillBeChosen.getReservedTime();
        assertEquals((long) orderCreated.getDuration(), reserveDiff);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoordinateOrder_noLaundries_exceptionIsThrown() throws Throwable {
        OrderCoordinationDto coordinationDto = new OrderCoordinationDto(0, Collections.emptyList());

        String coordinationDtoJson = objectMapper.writeValueAsString(coordinationDto);
        try {
            mockMvc.perform(post("/orders")
                    .contentType(APPLICATION_JSON.toString())
                    .content(coordinationDtoJson));
        } catch (NestedServletException e) {
            throw e.getCause();
        }
        fail();
    }


    /**
     * Checks that in case of submitting DTO with outdated laundry state (earlier version) the laundry state in DB doesn't change
     * Input: {@link InboundLaundryStateDto} with an earlier version than the current one
     * Output: laundry state doesn't change
     *
     * @throws Exception if object cannot be serialized
     */
    @Test
    @Sql(scripts = "/test-data/basic.sql")
    @Transactional
    public void testProcessSubmittedOrder_earlierVersion_queueWaitingTimeNotChanged() throws Exception {
        LaundryStateDto initialLaundryState = laundryStateService.getLaundryStateById(EXISTING_LAUNDRY_ID);
        OrderDto reservedOrder = orderService.getOrderById(EXISTING_RESERVED_ORDER_ID);

        InboundLaundryStateDto inboundLaundryStateDto = new InboundLaundryStateDto(EXISTING_LAUNDRY_ID,
                INBOUND_LAUNDRY_WAITING_TIME,
                initialLaundryState.getVersion() - 1);
        OrderSubmittedDto orderSubmittedDto = new OrderSubmittedDto(EXISTING_RESERVED_ORDER_ID, inboundLaundryStateDto);

        String orderSubmittedJson = objectMapper.writeValueAsString(orderSubmittedDto);
        mockMvc.perform(put("/orders/" + EXISTING_RESERVED_ORDER_ID + "/status/submitted")
                .contentType(APPLICATION_JSON.toString())
                .content(orderSubmittedJson))
                .andExpect(status().isOk());

        LaundryStateDto updatedLaundryState = laundryStateService.getLaundryStateById(EXISTING_LAUNDRY_ID);
        assertEquals(initialLaundryState.getVersion(), updatedLaundryState.getVersion());
        assertEquals(initialLaundryState.getQueueWaitingTime(), updatedLaundryState.getQueueWaitingTime());
        assertEquals(initialLaundryState.getReservedTime() - reservedOrder.getDuration(), updatedLaundryState.getReservedTime().longValue());
        assertEquals(OrderStatus.SUBMITTED, orderService.getOrderById(EXISTING_RESERVED_ORDER_ID).getStatus());
    }

    @Test
    @Sql(scripts = "/test-data/basic.sql")
    @Transactional
    public void testProcessSubmittedOrder_orderIsReserved_orderIsSubmitted() throws Exception {
        Long existingOrderDuration = orderService.getOrderById(EXISTING_RESERVED_ORDER_ID).getDuration();
        Long reservedTimeBeforeOrderSubmitted = laundryStateService.getLaundryStateById(EXISTING_LAUNDRY_ID).getReservedTime();

        InboundLaundryStateDto inboundLaundryStateDto = new InboundLaundryStateDto(
                EXISTING_LAUNDRY_ID,
                INBOUND_LAUNDRY_WAITING_TIME,
                INBOUND_LAUNDRY_STATE_VERSION);
        OrderSubmittedDto orderSubmittedDto = new OrderSubmittedDto(EXISTING_RESERVED_ORDER_ID, inboundLaundryStateDto);

        String orderSubmittedJson = objectMapper.writeValueAsString(orderSubmittedDto);
        mockMvc.perform(put("/orders/" + EXISTING_RESERVED_ORDER_ID + "/status/submitted")
                .contentType(APPLICATION_JSON.toString())
                .content(orderSubmittedJson))
                .andExpect(status().isOk());

        LaundryStateDto updatedLaundryState = laundryStateService.getLaundryStateById(EXISTING_LAUNDRY_ID);

        assertEquals(INBOUND_LAUNDRY_WAITING_TIME, updatedLaundryState.getQueueWaitingTime());
        assertEquals(reservedTimeBeforeOrderSubmitted - existingOrderDuration, (long) updatedLaundryState.getReservedTime());
        assertEquals(INBOUND_LAUNDRY_STATE_VERSION, updatedLaundryState.getVersion());
        assertEquals(OrderStatus.SUBMITTED, orderService.getOrderById(EXISTING_RESERVED_ORDER_ID).getStatus());
    }

    @Test
    @Sql(scripts = "/test-data/basic.sql")
    @Transactional
    public void testProcessCompletedOrder_orderIsSubmitted_orderIsProcessed() throws Exception {
        Long reservedTimeBeforeOrderSubmitted = laundryStateService.getLaundryStateById(EXISTING_LAUNDRY_ID).getReservedTime();

        InboundLaundryStateDto inboundLaundryStateDto = new InboundLaundryStateDto(
                EXISTING_LAUNDRY_ID,
                INBOUND_LAUNDRY_WAITING_TIME,
                INBOUND_LAUNDRY_STATE_VERSION);

        OrderCompletedDto orderCompletedDto = new OrderCompletedDto(EXISTING_SUBMITTED_ORDER_ID,
                inboundLaundryStateDto, DEFAULT_COMPLETION_TIME);

        String orderSubmittedJson = objectMapper.writeValueAsString(orderCompletedDto);
        mockMvc.perform(put("/orders/" + EXISTING_SUBMITTED_ORDER_ID + "/status/completed")
                .contentType(APPLICATION_JSON.toString())
                .content(orderSubmittedJson))
                .andExpect(status().isOk());

        LaundryStateDto updatedLaundryState = laundryStateService.getLaundryStateById(EXISTING_LAUNDRY_ID);

        assertEquals(INBOUND_LAUNDRY_WAITING_TIME, updatedLaundryState.getQueueWaitingTime());
        assertEquals(reservedTimeBeforeOrderSubmitted, updatedLaundryState.getReservedTime());
        assertEquals(INBOUND_LAUNDRY_STATE_VERSION, updatedLaundryState.getVersion());
        assertEquals(OrderStatus.COMPLETED, orderService.getOrderById(EXISTING_SUBMITTED_ORDER_ID).getStatus());
    }

    /**
     * Checks that in case of an attempt of processing order with wrong status (not {@link OrderStatus#SUBMITTED}),
     * the exception is thrown and DB is not changed.
     * Input: order with {@link OrderStatus#RESERVED}
     * Output: error, because only order with {@link OrderStatus#SUBMITTED} can be processed (or change it's status
     * to {@link OrderStatus#COMPLETED} using PUT /orders/{id}/status/completed
     *
     * @throws Exception if object cannot be serialized
     */
    @Test
    @Sql(scripts = "/test-data/basic.sql")
    @Transactional
    public void testProcessCompletedOrder_orderIsReserved_orderIsNotProcessedDbIsNotChanged() throws Exception {
        InboundLaundryStateDto inboundLaundryStateDto = new InboundLaundryStateDto(
                EXISTING_LAUNDRY_ID,
                INBOUND_LAUNDRY_WAITING_TIME,
                INBOUND_LAUNDRY_STATE_VERSION);

        OrderCompletedDto orderCompletedDto = new OrderCompletedDto(
                EXISTING_RESERVED_ORDER_ID,
                inboundLaundryStateDto,
                DEFAULT_COMPLETION_TIME);

        LaundryStateDto initialLaundryState = laundryStateService.getLaundryStateById(EXISTING_LAUNDRY_ID);
        OrderDto initialOrder = orderService.getOrderById(EXISTING_RESERVED_ORDER_ID);

        String orderSubmittedJson = objectMapper.writeValueAsString(orderCompletedDto);
        try {
            Exception mvcResult = mockMvc.perform(put("/orders/" + EXISTING_RESERVED_ORDER_ID + "/status/completed")
                    .contentType(APPLICATION_JSON.toString())
                    .content(orderSubmittedJson))
                    .andReturn().getResolvedException();
            if (mvcResult != null) {
                throw mvcResult;
            } else {
                fail();
            }
        } catch (Exception e) {
            LaundryStateDto updatedLaundryState = laundryStateService.getLaundryStateById(EXISTING_LAUNDRY_ID);
            OrderDto updatedOrder = orderService.getOrderById(EXISTING_RESERVED_ORDER_ID);

            assertEquals(initialLaundryState, updatedLaundryState);
            assertEquals(initialOrder, updatedOrder);
        }

    }

    @Test(expected = MethodArgumentNotValidException.class)
    public void testProcessCompletedOrder_invalidQueueWaitingTime_orderValidationExceptionIsThrown() throws Exception {
        InboundLaundryStateDto inboundLaundryStateDto = new InboundLaundryStateDto(
                EXISTING_LAUNDRY_ID,
                -100L,
                INBOUND_LAUNDRY_STATE_VERSION);

        OrderCompletedDto orderCompletedDto = new OrderCompletedDto(
                EXISTING_SUBMITTED_ORDER_ID,
                inboundLaundryStateDto,
                DEFAULT_COMPLETION_TIME);

        String orderSubmittedJson = objectMapper.writeValueAsString(orderCompletedDto);
        MvcResult mvcResult = mockMvc.perform(put("/orders/" + EXISTING_SUBMITTED_ORDER_ID + "/status/completed")
                .contentType(APPLICATION_JSON.toString())
                .content(orderSubmittedJson))
                .andExpect(status().is4xxClientError())
                .andReturn();
        throw Objects.requireNonNull(mvcResult.getResolvedException());
    }
}
