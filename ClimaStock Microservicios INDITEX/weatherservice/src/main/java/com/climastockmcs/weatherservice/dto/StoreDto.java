package com.climastockmcs.weatherservice.dto;


public record StoreDto(Long id, String name, String city, String country,
                       Double latitude, Double longitude) {
}