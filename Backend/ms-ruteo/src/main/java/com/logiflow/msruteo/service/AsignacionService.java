package com.logiflow.msruteo.service;

import com.logiflow.msruteo.dto.VehiculoDisponibleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Algoritmo de selección de vehículo según nivel geográfico y capacidad.
 *
 * Reglas:
 *   LOCAL      → prefiere MOTO o AUTO  (fallback: cualquier disponible)
 *   PROVINCIAL → prefiere FURGONETA    (fallback: CAMION)
 *   NACIONAL   → prefiere CAMION       (fallback: cualquier disponible)
 *
 * Entre los candidatos del tipo preferido, se elige el de menor capacidad
 * que aún supere el pesoKg del paquete (more efficient fit).
 */
@Service
@Slf4j
public class AsignacionService {

    private static final Set<String> TIPOS_LOCAL      = Set.of("MOTO", "AUTO");
    private static final Set<String> TIPOS_PROVINCIAL = Set.of("FURGONETA");
    private static final Set<String> TIPOS_NACIONAL   = Set.of("CAMION");

    /**
     * Selecciona el vehículo más adecuado de la lista.
     *
     * @param vehiculos      lista obtenida de ms-flota (ya filtrada por capacidad >= pesoKg)
     * @param nivelGeografico LOCAL | PROVINCIAL | NACIONAL
     * @param pesoKg          peso del paquete en kg
     * @return vehículo seleccionado
     */
    public Optional<VehiculoDisponibleDTO> seleccionar(
            List<VehiculoDisponibleDTO> vehiculos,
            String nivelGeografico,
            Double pesoKg) {

        if (vehiculos == null || vehiculos.isEmpty()) {
            return Optional.empty();
        }

        // Filtrar por capacidad suficiente (ms-flota ya lo hace, pero doble garantía)
        List<VehiculoDisponibleDTO> aptos = vehiculos.stream()
                .filter(v -> v.getCapacidad() >= pesoKg)
                .toList();

        if (aptos.isEmpty()) {
            return Optional.empty();
        }

        Set<String> tiposPreferidos = switch (nivelGeografico.toUpperCase()) {
            case "LOCAL"      -> TIPOS_LOCAL;
            case "PROVINCIAL" -> TIPOS_PROVINCIAL;
            case "NACIONAL"   -> TIPOS_NACIONAL;
            default           -> Set.of();
        };

        // Intentar con tipo preferido primero
        Optional<VehiculoDisponibleDTO> candidato = aptos.stream()
                .filter(v -> tiposPreferidos.contains(v.getTipo().toUpperCase()))
                .min(Comparator.comparingDouble(VehiculoDisponibleDTO::getCapacidad));

        if (candidato.isPresent()) {
            log.info("Vehículo seleccionado (tipo preferido {}): id={}, tipo={}, capacidad={}",
                    tiposPreferidos, candidato.get().getId(),
                    candidato.get().getTipo(), candidato.get().getCapacidad());
            return candidato;
        }

        // Fallback: cualquier vehículo apto con menor capacidad suficiente
        Optional<VehiculoDisponibleDTO> fallback = aptos.stream()
                .min(Comparator.comparingDouble(VehiculoDisponibleDTO::getCapacidad));

        fallback.ifPresent(v -> log.warn(
                "Fallback: sin tipo preferido para {}. Usando id={}, tipo={}, capacidad={}",
                nivelGeografico, v.getId(), v.getTipo(), v.getCapacidad()));

        return fallback;
    }

    /**
     * Estimación simple de kilómetros basada en nivel geográfico.
     * En producción se reemplazaría con una API de mapas real.
     */
    public double estimarKms(String nivelGeografico) {
        return switch (nivelGeografico.toUpperCase()) {
            case "LOCAL"      -> 15.0 + Math.random() * 20;
            case "PROVINCIAL" -> 80.0 + Math.random() * 120;
            case "NACIONAL"   -> 400.0 + Math.random() * 600;
            default           -> 50.0;
        };
    }
}
