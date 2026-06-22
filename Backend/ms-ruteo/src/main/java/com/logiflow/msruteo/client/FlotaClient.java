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
 *
 * RestTemplate se inyecta via constructor para permitir mockeo en tests
 * con @MockBean sin necesitar conexión real a ms-flota.
 */
@Component
@Slf4j
public class FlotaClient {

    private final RestTemplate restTemplate;

    @Value("${ms-flota.base-url}")
    private String flotaBaseUrl;

    /**
     * Constructor para inyección de dependencias.
     * En producción Spring inyecta el bean RestTemplate del contexto.
     * En tests, @MockBean sustituye este bean automáticamente.
     */
    public FlotaClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Obtiene la lista de vehículos disponibles de ms-flota.
     *
     * @param capacidadMinima peso mínimo en kg (opcional)
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
