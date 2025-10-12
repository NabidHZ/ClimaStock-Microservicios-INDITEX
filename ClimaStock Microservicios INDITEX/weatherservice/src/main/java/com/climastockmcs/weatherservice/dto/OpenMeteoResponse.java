package com.climastockmcs.weatherservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

//es un DTO  diseñado para recibir y mapear la respuesta JSON que
// devuelve la API de Open-Meteo cuando consultas el clima por coordenada


@Data //Es de lombok, genera getter y setter automáticamente
@JsonIgnoreProperties(ignoreUnknown = true) //Esto indica que si el JSON que llega tiene campos que mi clase no tiene definidos, los ignora sin lanzar error
public class OpenMeteoResponse {
    private double latitude;
    private double longitude;
    private Daily daily; //datos meteorológicos diarios (temperatura, lluvia, etc.), representados por una clase interna.

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Daily { //Esta clase representa la parte daily del JSON de Open-Meteo.
        private List<String> time; //fechas de los datos diarios.

        //@JjsonProerty Esto le dice a Jackson (la librería que convierte JSON a objetos Java) que:
        //el campo JSON llamado "temperature_2m_max" debe mapearse al atributo
        @JsonProperty("temperature_2m_max") //temperaturas máximas por día
        private List<Double> temperature2mMax;

        @JsonProperty("precipitation_probability_max") //probabilidad máxima de precipitación por día
        private List<Integer> precipitationProbabilityMax;
    }
}
