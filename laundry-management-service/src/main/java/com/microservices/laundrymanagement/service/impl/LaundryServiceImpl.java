//package com.microservices.laundrymanagement.service.impl;
//
//import com.microservices.laundrymanagement.entity.LaundryStateEntity;
//import com.microservices.laundrymanagement.entity.OrderEntity;
//import com.microservices.laundrymanagement.repository.LaundryRepository;
//import com.microservices.laundrymanagement.repository.OrderRepository;
//import com.microservices.laundrymanagement.service.LaundryService;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class LaundryServiceImpl implements LaundryService {
//    private OrderRepository orderRepository;
//    private LaundryRepository laundryRepository;
//
//    public LaundryServiceImpl(OrderRepository orderRepository, LaundryRepository laundryRepository) {
//        this.orderRepository = orderRepository;
//        this.laundryRepository = laundryRepository;
//    }
//
//    @Override
//    public List<LaundryStateEntity> getAllLaundries() {
//        List<LaundryStateEntity> laundries = new ArrayList<>();
//        laundryRepository.findAll().forEach(laundries::add);
//        return laundries;
//    }
//
//    @Override
//    public Optional<LaundryStateEntity> getById(int id) {
//        return laundryRepository.findById(id);
//    }
//}
