package com.climastockmcs.weatherservice.controller;


import com.climastockmcs.weatherservice.dto.WeatherResponse;
import com.climastockmcs.weatherservice.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@RestController //convierte esta clase en un controlador REST, es decir, una clase que escucha peticiones HTTP (como GET, POST, etc.) y devuelve respuestas JSON.
@RequestMapping("/api/weather")
public class WeatherController {

    //Aquí se está inyectando la lógica de negocio, es decir, el WeatherService
    private final WeatherService service;

    public WeatherController(WeatherService service) {
        this.service = service;
    }

    // Obtener el clima para una tienda específica
    @GetMapping("/store/{storeId}")
    public Mono<ResponseEntity<WeatherResponse>> getForStore(
            @PathVariable Long storeId, //captura el valor de {storeId} de la URL y lo asigna al parámetro storeId
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) { //Lee el encabezado HTTP "Authorization" (por si el cliente manda un token JWT).
        return service.getWeatherForStore(storeId, authorizationHeader)
                .map(resp -> ResponseEntity.ok(resp));
    }

    @GetMapping("/stores")
    public Flux<WeatherResponse> getForAllStores() { //Hace un GET para obtener el clima de todas las tiendas
        return service.getWeatherForAllStores();
    }

    // Para llamadas directas por coordenadas si lo necesitas:
    @GetMapping
    public Mono<ResponseEntity<WeatherResponse>> getByCoords(@RequestParam double lat, @RequestParam double lon) {
        return service.getWeatherForStoreByCoords(lat, lon)
                .map(resp -> ResponseEntity.ok(resp));
    }
}
