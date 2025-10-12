package com.climastockmcs.weatherservice.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration //@Configuration es un bean especial, este sirve para contener otros beans
@EnableCaching //Habilita el soporte de caché en la aplicación Spring.
public class CacheConfig {

    @Value("${cache.weather.expire-minutes:15}")//Esto lee el valor de cache.weather.expire-minutes del archivo
    // application.yml, si no está definido usa 15
    private int expireMinutes;

    @Value("${cache.weather.maximum-size:1000}")
    private int maximumSize;

    @Bean //Le dice a Spring: “guarda el resultado de este método en el contenedor de Beans para poder inyectarlo en otras clases”.
    public Caffeine<Object, Object> caffeineConfig() { //Configura y devuelve un objeto Caffeine
        return Caffeine.newBuilder() //Devuelve un builder, un objeto que sirve para configurar la caché paso a paso antes de “construirla
                .expireAfterWrite(expireMinutes, TimeUnit.MINUTES)
                .maximumSize(maximumSize);
    }

    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        //CacheManager es el administrador de caches: gestiona diferentes caches, se encarga de leer/escribir datos, borrarlos cuando caducan, etc
        CaffeineCacheManager manager = new CaffeineCacheManager("weatherByStore", "weatherByCoords");
        manager.setCaffeine(caffeine); //Usa el builder de Caffeine que configuré antes para construir los caches.
        return manager;
    }
}
