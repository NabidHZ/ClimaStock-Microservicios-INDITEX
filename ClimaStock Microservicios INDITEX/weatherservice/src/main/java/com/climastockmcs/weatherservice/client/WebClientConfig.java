package com.climastockmcs.weatherservice.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration //@Configuration es un Bean, que sirve para definir otros Beans
public class WebClientConfig {

    //@Value es una anotación que se utiliza para inyectar valores de propiedades desde archivos de configuración
    @Value("${open-meteo.base-url}") //Inyecta el valor de la propiedad open-meteo.base-url desde application.yml
    private String openMeteoBaseUrl;


    @Bean
    public WebClient.Builder webClientBuilder() { //Esto crea un “constructor” de clientes WebClient. como una “fábrica” para crear clientes configurables

        return WebClient.builder();
    }


    @Bean("openMeteoClient")
    public WebClient openMeteoClient(WebClient.Builder builder) {
        return builder.baseUrl(openMeteoBaseUrl).build();
    }
}
