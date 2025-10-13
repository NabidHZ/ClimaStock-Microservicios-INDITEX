package com.climastockmcs.weatherservice.controller;


import com.climastockmcs.weatherservice.exception.WeatherException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice //cada vez que ocurra una excepción en cualquier @RestController, pasa por aquí para manejarla
public class GlobalExceptionHandler {

    //Si se lanza una excepción de tipo WeatherException, ejecuta este método
    @ExceptionHandler(WeatherException.class)
    public ResponseEntity<String> handleWeatherException(WeatherException ex) { //ResponseEntity es una clase que representa toda la respuesta HTTP
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getMessage());
    }//Si salta este error, significa que la API del tiempo no está funcionando y ejecuta 503 Service Unavailable

    //Si en cualquier parte del programa se lanza una excepción del tipo IllegalArgumentException, ejecuta este método
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }//Crea una respuesta HTTP con código 400 Bad Request

    //Maneja cualquier otra excepción no prevista
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + ex.getMessage());

    }//Crea una respuesta HTTP con código 500 Internal Server Error
}
