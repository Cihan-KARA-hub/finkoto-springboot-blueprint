package com.finkoto.gatewayserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class MergeController {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/combined")
    public Mono<String> getCombinedResponse() {
        // Service 1 URL
        Mono<String> service1Response = webClientBuilder.build()
                .get()
                .uri("http://localhost:8087/ocpp-mock-server/csm/v1/mock-charge-points")  // Service 1 URL
                .retrieve()
                .bodyToMono(String.class);

        // Service 2 URL
        Mono<String> service2Response = webClientBuilder.build()
                .get()
                .uri("http://localhost:8086/charge-station/csm/v1/connectors")  // Service 2 URL
                .retrieve()
                .bodyToMono(String.class);

        // İki Mono'yu birleştiriyoruz
        return Mono.zip(service1Response, service2Response)
                .map(tuple -> {
                    String response1 = tuple.getT1();
                    String response2 = tuple.getT2();
                    return "Service 1 Response: " + response1 + ", Service 2 Response: " + response2;
                });
    }
}
