package com.proyecto1P.logiflow.services.implement;

import com.proyecto1P.logiflow.dtos.ConductorDTO;
import com.proyecto1P.logiflow.entity.Conductor;
import com.proyecto1P.logiflow.entity.EstadoConductor;
import com.proyecto1P.logiflow.exception.ConductorNotFoundException;
import com.proyecto1P.logiflow.repository.ConductorRepository;
import com.proyecto1P.logiflow.services.ConductorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Conductores.
 * - Mapea entre DTO y Entity.
 * - Asegura que al crear un conductor se asigne un estado por defecto (ACTIVO)
 *   si el DTO no trae estado.
 * - Al actualizar, sólo sobrescribe el estado si el DTO trae un valor (evita poner null).
 */
@Service
@Transactional
public class ConductorServiceImpl implements ConductorService {

    private final ConductorRepository repository;

    public ConductorServiceImpl(ConductorRepository repository) {
        this.repository = repository;
    }

    /**
     * Convierte entidad a DTO.
     */
    private ConductorDTO toDTO(Conductor c) {
        return new ConductorDTO(c.getId(), c.getNombre(), c.getApellido(), c.getCedula(), c.getLicencia(), c.getTelefono(), c.getEmail(), c.getEstado());
    }

    /**
     * Convierte DTO a entidad. Si el DTO no incluye estado, se asigna ACTIVO por defecto.
     */
    private Conductor toEntity(ConductorDTO dto) {
        Conductor c = new Conductor();
        c.setId(dto.getId());
        c.setNombre(dto.getNombre());
        c.setApellido(dto.getApellido());
        c.setCedula(dto.getCedula());
        c.setLicencia(dto.getLicencia());
        c.setTelefono(dto.getTelefono());
        c.setEmail(dto.getEmail());
        c.setEstado(dto.getEstado() != null ? dto.getEstado() : EstadoConductor.ACTIVO);
        return c;
    }

    @Override
    public List<ConductorDTO> obtenerTodos() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public ConductorDTO obtenerPorId(Long id) {
        Conductor c = repository.findById(id)
                .orElseThrow(() -> new ConductorNotFoundException("Conductor no encontrado con id: " + id));
        return toDTO(c);
    }

    @Override
    public ConductorDTO guardar(ConductorDTO conductorDTO) {
        Conductor c = toEntity(conductorDTO);
        Conductor saved = repository.save(c);
        return toDTO(saved);
    }

    @Override
    public ConductorDTO actualizar(Long id, ConductorDTO conductorDTO) {
        Conductor existente = repository.findById(id)
                .orElseThrow(() -> new ConductorNotFoundException("Conductor no encontrado con id: " + id));
        existente.setNombre(conductorDTO.getNombre());
        existente.setApellido(conductorDTO.getApellido());
        existente.setCedula(conductorDTO.getCedula());
        existente.setLicencia(conductorDTO.getLicencia());
        existente.setTelefono(conductorDTO.getTelefono());
        existente.setEmail(conductorDTO.getEmail());
        if (conductorDTO.getEstado() != null) {
            existente.setEstado(conductorDTO.getEstado());
        }
        Conductor updated = repository.save(existente);
        return toDTO(updated);
    }

    @Override
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ConductorNotFoundException("Conductor no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }
}