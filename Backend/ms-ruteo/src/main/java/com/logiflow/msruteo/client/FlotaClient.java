package com.logiflow.msruteo.client;

import com.logiflow.msruteo.dto.VehiculoDisponibleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * Cliente REST para consumir ms-flota-rest.
 * Endpoint consumido: GET /api/vehiculos/disponibles?capacidadMinima=X
 */
@Component
@Slf4j
public class FlotaClient {

    private final RestTemplate restTemplate;

    @Value("${ms-flota.base-url}")
    private String flotaBaseUrl;

    public FlotaClient() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Obtiene la lista de vehículos disponibles de ms-flota.
     * Filtra por capacidad mínima cuando se especifica.
     *
     * @param capacidadMinima peso del paquete en kg (opcional)
     * @return lista de vehículos disponibles, vacía si falla la llamada
     */
    public List<VehiculoDisponibleDTO> obtenerVehiculosDisponibles(Double capacidadMinima) {
        try {
            String url = flotaBaseUrl + "/api/vehiculos/disponibles";
            if (capacidadMinima != null) {
                url += "?capacidadMinima=" + capacidadMinima;
            }

            ResponseEntity<List<VehiculoDisponibleDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            List<VehiculoDisponibleDTO> vehiculos = response.getBody();
            log.info("ms-flota retornó {} vehículos disponibles", vehiculos != null ? vehiculos.size() : 0);
            return vehiculos != null ? vehiculos : Collections.emptyList();

        } catch (Exception e) {
            log.error("Error al consultar ms-flota: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
