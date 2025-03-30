package com.example.service_reservation.client;

import com.example.service_reservation.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class CustomerClient {

    private final RestTemplate restTemplate;
    private final String customerServiceUrl;

    @Autowired
    public CustomerClient(RestTemplateBuilder builder, 
                        @Value("${service.customer.url}") String customerServiceUrl) {
        this.restTemplate = builder.build();
        this.customerServiceUrl = customerServiceUrl;
    }

    public CustomerDTO getCustomerById(Long customerId) {
        String url = UriComponentsBuilder.fromHttpUrl(customerServiceUrl + "/customers/{id}")
                                         .buildAndExpand(customerId)
                                         .toUriString();
        return restTemplate.getForObject(url, CustomerDTO.class);
    }
}
