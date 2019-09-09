package com.microservices.laundrymanagement.mapper;

import com.microservices.laundrymanagement.configuration.MapperConfig;
import com.microservices.laundrymanagement.dto.DetailSubmissionDto;
import com.microservices.laundrymanagement.dto.OrderSubmissionDto;
import com.microservices.laundrymanagement.entity.OrderEntity;
import com.microservices.laundrymanagement.entity.OrderStatus;
import com.microservices.laundrymanagement.util.GenericMapper;
import com.microservices.laundrymanagement.util.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Test class for mapping testing purposes. Mappers that are being tested are constructing with
 * Spring's configuration classes, but Spring's context is not used. Class has four methods for testing
 * all of {@link Mapper}'s methods. But new methods can be added in case of adding custom {@link Converter}s to
 * {@link ModelMapper} instance.Every instance of {@link Mapper}'s implementation has to be constructed
 * inside setUp method with the same modelMapper instance.
 *
 * @see Mapper
 * @see GenericMapper
 * @see MapperConfig
 */
@RunWith(MockitoJUnitRunner.class)
public class MapperTest {
    private static final int BUCKET_ID = 66;
    private static final int LAUNDRY_ID = 3;
    private static final int ORDER_ID = 5;
    /**
     * Model mapper instance for tested mappers
     */
    private final ModelMapper modelMapper = new ModelMapper();
    /**
     * Reference to a tested orderMapper
     */
    private Mapper<OrderEntity, OrderSubmissionDto> orderMapper;

    /**
     * Method sets up projectMapper configuration without spring context
     */
    @Before
    public void setUp() {
        orderMapper = new MapperConfig().orderMapper(modelMapper);
    }

    @Test
    public void testMapOrder_noDetails_allMappedTimeIsZero() {
        OrderSubmissionDto orderSubmissionDto = new OrderSubmissionDto();
        orderSubmissionDto.setBucket(BUCKET_ID);
        orderSubmissionDto.setLaundryId(LAUNDRY_ID);
        orderSubmissionDto.setOrderId(ORDER_ID);
        orderSubmissionDto.setDetails(new ArrayList<>());

        OrderEntity orderEntity = orderMapper.mapToEntity(orderSubmissionDto);
        assertEquals(orderEntity.getBucket(), BUCKET_ID);
        assertEquals(orderEntity.getLaundryId(), LAUNDRY_ID);
        assertEquals(orderEntity.getId(), ORDER_ID);
        assertEquals(orderEntity.getEstimatedTime(), 0);
    }

    @Test
    public void testMapOrder_detailsAreNull_estimatedTimeIsZero() {
        OrderSubmissionDto orderSubmissionDto = new OrderSubmissionDto();
        orderSubmissionDto.setBucket(BUCKET_ID);
        orderSubmissionDto.setLaundryId(LAUNDRY_ID);
        orderSubmissionDto.setOrderId(ORDER_ID);
        orderSubmissionDto.setDetails(null);

        OrderEntity orderEntity = orderMapper.mapToEntity(orderSubmissionDto);
        assertEquals(orderEntity.getBucket(), BUCKET_ID);
        assertEquals(orderEntity.getLaundryId(), LAUNDRY_ID);
        assertEquals(orderEntity.getId(), ORDER_ID);
        assertEquals(orderEntity.getEstimatedTime(), 0);
    }

    @Test
    public void testMapOrder_hasDetails_estimatedTimeIsSummed() {
        OrderSubmissionDto orderSubmissionDto = new OrderSubmissionDto();
        orderSubmissionDto.setBucket(BUCKET_ID);
        orderSubmissionDto.setLaundryId(LAUNDRY_ID);
        orderSubmissionDto.setOrderId(ORDER_ID);
        DetailSubmissionDto detail1 = new DetailSubmissionDto();
        detail1.setDuration(200L);
        DetailSubmissionDto detail2 = new DetailSubmissionDto();
        detail2.setDuration(2L);
        orderSubmissionDto.setDetails(Arrays.asList(detail1, detail2));

        OrderEntity orderEntity = orderMapper.mapToEntity(orderSubmissionDto);
        assertEquals(orderEntity.getBucket(), BUCKET_ID);
        assertEquals(orderEntity.getLaundryId(), LAUNDRY_ID);
        assertEquals(orderEntity.getId(), ORDER_ID);
        assertEquals(orderEntity.getEstimatedTime(), 202);
    }

    @Test
    public void testMapOrder_allIsNull_entityHasDefaultValues() {
        OrderEntity orderEntity = orderMapper.mapToEntity(new OrderSubmissionDto());

        assertEquals(orderEntity.getId(), 0);
        assertEquals(orderEntity.getLaundryId(), 0);
        assertEquals(orderEntity.getBucket(), 0);
        assertEquals(orderEntity.getStatus(), OrderStatus.QUEUED);
        assertEquals(orderEntity.getEstimatedTime(), 0);
    }
}
