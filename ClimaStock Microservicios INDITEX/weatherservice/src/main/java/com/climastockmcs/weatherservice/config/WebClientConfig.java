package com.climastockmcs.weatherservice.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


//Esta clase es la configuración del cliente HTTP que mi microservicio de clima (WeatherService)
// usa para comunicarse con la API externa de Open-Meteo (donde saca los datos del tiempo).

@Configuration //@Configuration es un bean especial, este sirve para contener otros beans
public class WebClientConfig {


    //@Value le dice a Spring que inyecte un valor del archivo application.yml
    @Value("${open-meteo.base-url}") //Esto lee el valor de open-meteo.base-url del archivo application.yml
    private String openMeteoBaseUrl;

    @Bean
    public WebClient.Builder webClientBuilder() { //Este método crea y devuelve un bean de tipo WebClient.Builder
        return WebClient.builder();
    }

    @Bean("openMeteoClient")
    public WebClient openMeteoClient(WebClient.Builder builder) { // Spring te pasa el builder que defini arriba
        // (lo inyecta automáticamente porque también es un Bean

        return builder.baseUrl(openMeteoBaseUrl).build();//Configura el WebClient con la URL base de Open-Meteo y lo construye.
    }
}
