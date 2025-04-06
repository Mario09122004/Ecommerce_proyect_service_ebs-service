package com.utd.ti.soa.ebs_service.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.utd.ti.soa.ebs_service.utils.Auth;

import reactor.core.publisher.Mono;
import com.utd.ti.soa.ebs_service.model.Client;

@RestController
@RequestMapping("/api/v1/esb")
public class ESBcontrollerClient {
    private final WebClient webClient = WebClient.create();
    private final Auth auth = new Auth();

    @PostMapping(value = "/client", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> createClient(@RequestBody Client client) {

        System.out.println("Enviando solicitud a Node.js user");

        return webClient.post()
                .uri("http://ecommerceproyectserviceclient-production.up.railway.app:37257/api/client")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(client)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    System.out.println("✅ Respuesta del servicio Node.js: " + response);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    return ResponseEntity.ok().headers(headers).body(response);
                })
                .onErrorResume(WebClientResponseException.class,
                        e -> Mono.just(ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString())))
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor")));
    }

    @GetMapping(value = "/client", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> getAllClients(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (!auth.validToken(token)) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido"));
        }

        System.out.println("📤 Enviando solicitud a Node.js para obtener todos los clientes");

        return webClient.get()
                .uri("http://ecommerceproyectserviceclient-production.up.railway.app:37257/api/client")
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> ResponseEntity.ok().body(response))
                .onErrorResume(WebClientResponseException.class,
                        e -> Mono.just(ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString())))
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor")));
    }

    @PatchMapping(value = "/client/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> updateUser(@PathVariable("id") String id,
                                                   @RequestBody Client client,
                                                   @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (!auth.validToken(token)) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido"));
        }

        return webClient.patch()
                .uri("http://ecommerceproyectserviceclient-production.up.railway.app:37257/api/client/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(client)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> ResponseEntity.ok().body(response))
                .onErrorResume(WebClientResponseException.class,
                        e -> Mono.just(ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString())))
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor")));
    }

    @DeleteMapping(value = "/client/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> deleteUser(@PathVariable("id") String id,
                                                   @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (!auth.validToken(token)) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido"));
        }

        return webClient.delete()
                .uri("http://ecommerceproyectserviceclient-production.up.railway.app:37257/api/client/" + id)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> ResponseEntity.ok().body(response))
                .onErrorResume(WebClientResponseException.class,
                        e -> Mono.just(ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString())))
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor")));
    }
}