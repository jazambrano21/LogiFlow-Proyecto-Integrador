package com.proyecto1P.logiflow.services.implement;

import com.proyecto1P.logiflow.dtos.VehiculoDTO;
import com.proyecto1P.logiflow.entity.Conductor;
import com.proyecto1P.logiflow.entity.EstadoVehiculo;
import com.proyecto1P.logiflow.entity.Vehiculo;
import com.proyecto1P.logiflow.exception.ConductorNotFoundException;
import com.proyecto1P.logiflow.exception.VehiculoNotFoundException;
import com.proyecto1P.logiflow.repository.ConductorRepository;
import com.proyecto1P.logiflow.repository.VehiculoRepository;
import com.proyecto1P.logiflow.services.VehiculoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Vehículos.
 *
 * Reglas importantes:
 * - Si conductorId viene informado, se valida que exista antes de asignar.
 * - Si conductorId viene null, el vehículo queda sin conductor (desasignado).
 * - Se expone en DTO solo conductorId y nombreConductor.
 */
@Service
@Transactional
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ConductorRepository conductorRepository;

    public VehiculoServiceImpl(VehiculoRepository vehiculoRepository, ConductorRepository conductorRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.conductorRepository = conductorRepository;
    }

    private VehiculoDTO toDTO(Vehiculo v) {
        Long conductorId = null;
        String nombreConductor = null;

        if (v.getConductor() != null) {
            conductorId = v.getConductor().getId();
            nombreConductor = v.getConductor().getNombre() + " " + v.getConductor().getApellido();
        }

        return new VehiculoDTO(
                v.getId(),
                v.getMatricula(),
                v.getTipo(),
                v.getCapacidad(),
                v.getEstado(),
                conductorId,
                nombreConductor
        );
    }

    /**
     * Mapea datos simples; la asignación de conductor se maneja aparte
     * para validar existencia del conductor.
     */
    private Vehiculo toEntitySinConductor(VehiculoDTO dto) {
        Vehiculo v = new Vehiculo();
        v.setId(dto.getId());
        v.setMatricula(dto.getMatricula());
        v.setTipo(dto.getTipo());
        v.setCapacidad(dto.getCapacidad());
        v.setEstado(dto.getEstado() != null ? dto.getEstado() : EstadoVehiculo.DISPONIBLE);
        return v;
    }

    /**
     * Regla de asignación:
     * - conductorId != null -> validar y asignar.
     * - conductorId == null -> desasignar (set null).
     */
    private void aplicarAsignacionConductor(Vehiculo vehiculo, Long conductorId) {
        if (conductorId == null) {
            vehiculo.setConductor(null);
            return;
        }

        Conductor conductor = conductorRepository.findById(conductorId)
                .orElseThrow(() -> new ConductorNotFoundException("Conductor no encontrado con id: " + conductorId));

        vehiculo.setConductor(conductor);
    }

    @Override
    public List<VehiculoDTO> obtenerTodos() {
        return vehiculoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehiculoDTO> obtenerDisponibles() {
        return vehiculoRepository.findAll()
                .stream()
                .filter(v -> v.getEstado() == EstadoVehiculo.DISPONIBLE)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VehiculoDTO obtenerPorId(Long id) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new VehiculoNotFoundException("Vehículo no encontrado con id: " + id));
        return toDTO(vehiculo);
    }

    @Override
    public VehiculoDTO guardar(VehiculoDTO vehiculoDTO) {
        Vehiculo vehiculo = toEntitySinConductor(vehiculoDTO);
        aplicarAsignacionConductor(vehiculo, vehiculoDTO.getConductorId());

        Vehiculo guardado = vehiculoRepository.save(vehiculo);
        return toDTO(guardado);
    }

    @Override
    public VehiculoDTO actualizar(Long id, VehiculoDTO vehiculoDTO) {
        Vehiculo existente = vehiculoRepository.findById(id)
                .orElseThrow(() -> new VehiculoNotFoundException("Vehículo no encontrado con id: " + id));

        existente.setMatricula(vehiculoDTO.getMatricula());
        existente.setTipo(vehiculoDTO.getTipo());
        existente.setCapacidad(vehiculoDTO.getCapacidad());

        if (vehiculoDTO.getEstado() != null) {
            existente.setEstado(vehiculoDTO.getEstado());
        }

        aplicarAsignacionConductor(existente, vehiculoDTO.getConductorId());

        Vehiculo actualizado = vehiculoRepository.save(existente);
        return toDTO(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        if (!vehiculoRepository.existsById(id)) {
            throw new VehiculoNotFoundException("Vehículo no encontrado con id: " + id);
        }
        vehiculoRepository.deleteById(id);
    }
}