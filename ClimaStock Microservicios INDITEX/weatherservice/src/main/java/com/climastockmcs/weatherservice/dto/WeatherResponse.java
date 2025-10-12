package com.climastockmcs.weatherservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResponse {
    private Long storeId;
    private String storeName;
    private Double latitude;
    private Double longitude;
    private String date;
    private Double maxTemperatureC;
    private Integer rainProbabilityPercent;
}