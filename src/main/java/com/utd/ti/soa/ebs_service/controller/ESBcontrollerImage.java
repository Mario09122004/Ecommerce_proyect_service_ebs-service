package com.utd.ti.soa.ebs_service.controller;

import org.springframework.core.io.Resource;  // Para Resource
import org.springframework.core.io.ByteArrayResource;  // Para ByteArrayResource
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

@RestController
public class ESBcontrollerImage {
    private WebClient webClient = WebClient.create();

    // Constructor para inyectar WebClient
    public ESBcontrollerImage(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://api_products:3004").build();
    }

    @GetMapping("/product/image/{filename}")
    public Mono<ResponseEntity<Resource>> getImage(@PathVariable String filename) {
        return webClient.get()
                .uri("/uploads/" + filename)  // URL del servicio products
                .retrieve()
                .bodyToMono(Resource.class)  // Convertir la respuesta en un Resource
                .map(resource -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .contentType(MediaType.IMAGE_JPEG)  // Puedes ajustar el tipo de imagen según sea necesario
                        .body(resource))
                .onErrorResume(WebClientResponseException.class,
                        e -> Mono.just(ResponseEntity.status(e.getStatusCode()).body(null)))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));
    }
}
