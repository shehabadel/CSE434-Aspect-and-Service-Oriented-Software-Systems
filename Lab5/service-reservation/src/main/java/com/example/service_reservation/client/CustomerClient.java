package com.example.service_reservation.client;

import com.example.service_reservation.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class CustomerClient {

    private final RestTemplate restTemplate;

    @Autowired
    public CustomerClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CustomerDTO getCustomerById(Long customerId) {
        String url = UriComponentsBuilder.fromHttpUrl("http://service-customer:5000/customers/{id}")
                                         .buildAndExpand(customerId)
                                         .toUriString();
        return restTemplate.getForObject(url, CustomerDTO.class);
    }
}
