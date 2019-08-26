package com.microservices.usermanagement.api.feign;

import com.microservices.usermanagement.api.dto.UserDto;
import com.microservices.usermanagement.api.service.UserServiceApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Defines the parameters and paths of REST API of user management api
 * <p>
 * Java feign client will be generated based on this declaration.
 * <p>
 * We use placeholder in the {@link FeignClient#name()} field in order
 * for Ribbon load balancing client and Eureka discovery service to understand
 * the name of the service the request should be redirected to.
 */
@FeignClient(name = "${user.management.service.name}/users")
public interface UserServiceClient extends UserServiceApi {

    /**
     * {@inheritDoc}
     */
    @GetMapping(value = "{login}")
    UserDto getUserByLogin(@PathVariable String login);
}
