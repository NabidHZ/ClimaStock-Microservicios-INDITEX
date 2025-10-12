package com.climastockmcs.weatherservice.client;

import com.climastockmcs.weatherservice.dto.StoreDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component //
public class StoreClient {

    private final WebClient webClient;
    /*Webcliente es una clase de Spring que te permite hacer peticiones HTTP a otros servicios desde mi aplicación.
    Es como un “navegador” o un “Postman” dentro de tu código Java.
    Con él puedes hacer peticiones CRUD desde el microservicio:
    */


    public StoreClient(WebClient.Builder builder, //
                       @Value("${store-service.base-url:http://localhost:8082}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    } //Con buiilder configuro el WebClient con la URL base del store-service y lo construyo.



    public Mono<StoreDto> getStoreById(Long id, String bearerToken) {
        return webClient.get() //Hace una petición GET HTTP
                .uri("/stores/{id}", id) //La URL completa será http://localhost:8082/stores/{id}
                .accept(MediaType.APPLICATION_JSON)//Le dice al server que uiqre la respuesta en JSON
                .headers(h -> { //Aquí añado el token JWT a la cabecera Authorization
                    if (bearerToken != null && !bearerToken.isBlank()) { //Si el token no es nulo ni vacío añade Barer
                        h.set(HttpHeaders.AUTHORIZATION, bearerToken);
                    }
                })
                .retrieve()
                .bodyToMono(StoreDto.class); //Convierte la respuesta JSON en un objeto StoreDto
    }

    //Este método obtiene todos los stores (tiendas) del store-service
    public List<StoreDto> getAllStores() {
        // espera que store-service exponga GET /stores (lista)
        StoreDto[] arr = webClient.get()
                .uri("/stores")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(StoreDto[].class)//convierte ese JSON en un array de objetos
                .block(); //block() espera a que llegue la respuesta de forma síncrona
        return arr == null ? List.of() : Arrays.asList(arr);//Convierte el array en una lista y la devuelve
    }
}