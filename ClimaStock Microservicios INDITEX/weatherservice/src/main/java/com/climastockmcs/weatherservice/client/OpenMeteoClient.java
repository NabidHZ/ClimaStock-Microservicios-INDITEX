package com.climastockmcs.weatherservice.client;


import com.climastockmcs.weatherservice.dto.OpenMeteoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OpenMeteoClient {

    private final WebClient webClient;
    private final String timezone;
    private final int days;

    // Constructor injection of WebClient and configuration properties
    public OpenMeteoClient(WebClient openMeteoClient,
                           @Value("${open-meteo.timezone:auto}") String timezone,
                           @Value("${open-meteo.days:1}") int days) {
        this.webClient = openMeteoClient;
        this.timezone = timezone;
        this.days = days;
    }

    //Metodo para obtener el pronóstico diario del clima
    public OpenMeteoResponse getDailyForecast(double lat, double lon) {
        return webClient.get()// va a hacer una peticion GET
                .uri(uriBuilder -> uriBuilder //Construye la URL con los parámetros query
                        .queryParam("latitude", lat)
                        .queryParam("longitude", lon)
                        .queryParam("daily", "temperature_2m_max,precipitation_probability_max")
                        .queryParam("timezone", timezone)
                        .queryParam("forecast_days", days)
                        .build())//termina de construir la URL
                .retrieve()//Envia la paeticion al servidor
                .bodyToMono(OpenMeteoResponse.class)//Convierte la respuesta JSON en un objeto OpenMeteoResponse
                .block();//block() espera a que llegue la respuesta de forma síncrona
    }
}

