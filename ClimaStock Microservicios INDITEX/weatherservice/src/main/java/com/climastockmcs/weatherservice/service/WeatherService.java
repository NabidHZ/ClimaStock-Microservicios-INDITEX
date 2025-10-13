package com.climastockmcs.weatherservice.service;


import com.climastockmcs.weatherservice.client.OpenMeteoClient;
import com.climastockmcs.weatherservice.client.StoreClient;
import com.climastockmcs.weatherservice.dto.StoreDto;
import com.climastockmcs.weatherservice.dto.WeatherResponse;
import com.climastockmcs.weatherservice.dto.OpenMeteoResponse;
import com.climastockmcs.weatherservice.exception.WeatherException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    private final StoreClient storeClient;
    private final OpenMeteoClient openMeteoClient;

    // Inyección de dependencias vía constructor
    public WeatherService(StoreClient storeClient, OpenMeteoClient openMeteoClient) {
        this.storeClient = storeClient;
        this.openMeteoClient = openMeteoClient;
    }

    /**
     * Devuelve el clima para una tienda
     * Cacheado por storeId (configurable).
     */
    @Cacheable(value = "weatherByStore", key = "#storeId")
    public Mono<WeatherResponse> getWeatherForStore(Long storeId, String bearerToken) {
        return Mono.fromCallable(() -> {
            StoreDto store = storeClient.getStoreById(storeId, bearerToken).block();
            if (store == null || store.latitude() == null || store.longitude() == null) {
                throw new WeatherException("Tienda no encontrada o sin coordenadas para id=" + storeId);
            }
            OpenMeteoResponse resp = openMeteoClient.getDailyForecast(store.latitude(), store.longitude());
            if (resp == null || resp.getDaily() == null) {
                throw new WeatherException("No se pudo obtener predicción meteorológica para tienda " + storeId);
            }
            var daily = resp.getDaily();
            String date = (daily.getTime() != null && !daily.getTime().isEmpty()) ? daily.getTime().get(0) : null;
            Double maxTemp = (daily.getTemperature2mMax() != null && !daily.getTemperature2mMax().isEmpty())
                    ? daily.getTemperature2mMax().get(0) : null;
            Integer rainProb = (daily.getPrecipitationProbabilityMax() != null && !daily.getPrecipitationProbabilityMax().isEmpty())
                    ? daily.getPrecipitationProbabilityMax().get(0) : null;
            return new WeatherResponse(store.id(), store.name(), store.latitude(), store.longitude(),
                    date, maxTemp, rainProb);
        });
    }

    /**
     * Devuelve clima de todas las tiendas. Llamadas paralelas para acelerar.
     */
    //Hace los misomo que el metodo de arriba pero para todas las tiendas
    public Flux<WeatherResponse> getWeatherForAllStores() {
        List<StoreDto> stores = storeClient.getAllStores();
        return Flux.fromIterable(stores).flatMap(store -> Mono.fromCallable(() -> {
            try {
                OpenMeteoResponse resp = openMeteoClient.getDailyForecast(store.latitude(), store.longitude());
                var daily = resp.getDaily();
                String date = (daily.getTime() != null && !daily.getTime().isEmpty()) ? daily.getTime().get(0) : null;
                Double maxTemp = (daily.getTemperature2mMax() != null && !daily.getTemperature2mMax().isEmpty())
                        ? daily.getTemperature2mMax().get(0) : null;
                Integer rainProb = (daily.getPrecipitationProbabilityMax() != null && !daily.getPrecipitationProbabilityMax().isEmpty())
                        ? daily.getPrecipitationProbabilityMax().get(0) : null;
                return new WeatherResponse(store.id(), store.name(), store.latitude(), store.longitude(),
                        date, maxTemp, rainProb);
            } catch (Exception ex) {
                // en caso de error devolvemos objeto con nulls y continuamos
                return new WeatherResponse(store.id(), store.name(), store.latitude(), store.longitude(),
                        null, null, null);
            }
        }));
    }

    public Mono<WeatherResponse> getWeatherForStoreByCoords(double lat, double lon) {
        return Mono.fromCallable(() -> {
            OpenMeteoResponse resp = openMeteoClient.getDailyForecast(lat, lon);
            if (resp == null || resp.getDaily() == null) {
                throw new WeatherException("No se pudo obtener predicción meteorológica para las coordenadas dadas");
            }
            var daily = resp.getDaily();
            String date = (daily.getTime() != null && !daily.getTime().isEmpty()) ? daily.getTime().get(0) : null;
            Double maxTemp = (daily.getTemperature2mMax() != null && !daily.getTemperature2mMax().isEmpty())
                    ? daily.getTemperature2mMax().get(0) : null;
            Integer rainProb = (daily.getPrecipitationProbabilityMax() != null && !daily.getPrecipitationProbabilityMax().isEmpty())
                    ? daily.getPrecipitationProbabilityMax().get(0) : null;
            return new WeatherResponse(null, null, lat, lon, date, maxTemp, rainProb);
        });
    }
}
