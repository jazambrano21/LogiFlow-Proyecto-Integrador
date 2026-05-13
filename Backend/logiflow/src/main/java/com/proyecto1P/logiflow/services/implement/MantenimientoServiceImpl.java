package com.proyecto1P.logiflow.services.implement;

import com.proyecto1P.logiflow.dtos.MantenimientoDTO;
import com.proyecto1P.logiflow.entity.Conductor;
import com.proyecto1P.logiflow.entity.EstadoMantenimiento;
import com.proyecto1P.logiflow.entity.Mantenimiento;
import com.proyecto1P.logiflow.entity.Vehiculo;
import com.proyecto1P.logiflow.exception.MantenimientoNotFoundException;
import com.proyecto1P.logiflow.exception.MantenimientoVehiculoRequiredException;
import com.proyecto1P.logiflow.exception.VehiculoNotFoundException;
import com.proyecto1P.logiflow.repository.MantenimientoRepository;
import com.proyecto1P.logiflow.repository.VehiculoRepository;
import com.proyecto1P.logiflow.services.MantenimientoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MantenimientoServiceImpl implements MantenimientoService {

    private final MantenimientoRepository mantenimientoRepository;
    private final VehiculoRepository vehiculoRepository;

    public MantenimientoServiceImpl(MantenimientoRepository mantenimientoRepository,
                                    VehiculoRepository vehiculoRepository) {
        this.mantenimientoRepository = mantenimientoRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    /**
     * Convierte Entity -> DTO
     */
    private MantenimientoDTO toDTO(Mantenimiento mantenimiento) {

        Long vehiculoId = null;
        String matriculaVehiculo = null;

        Long conductorId = null;
        String nombreConductor = null;

        if (mantenimiento.getVehiculo() != null) {

            Vehiculo vehiculo = mantenimiento.getVehiculo();

            vehiculoId = vehiculo.getId();
            matriculaVehiculo = vehiculo.getMatricula();

            if (vehiculo.getConductor() != null) {

                Conductor conductor = vehiculo.getConductor();

                conductorId = conductor.getId();
                nombreConductor = conductor.getNombre()
                        + " "
                        + conductor.getApellido();
            }
        }

        return new MantenimientoDTO(
                mantenimiento.getId(),
                mantenimiento.getFecha(),
                mantenimiento.getDescripcion(),
                mantenimiento.getTipoMantenimiento(),
                mantenimiento.getCosto(),
                mantenimiento.getEstado(),
                vehiculoId,
                matriculaVehiculo,
                conductorId,
                nombreConductor
        );
    }

    /**
     * Convierte DTO -> Entity
     */
    private Mantenimiento toEntity(MantenimientoDTO dto) {

        Mantenimiento mantenimiento = new Mantenimiento();

        mantenimiento.setId(dto.getId());
        mantenimiento.setFecha(dto.getFecha());
        mantenimiento.setDescripcion(dto.getDescripcion());
        mantenimiento.setTipoMantenimiento(dto.getTipoMantenimiento());
        mantenimiento.setCosto(dto.getCosto());

        // Si no mandan estado -> PENDIENTE
        mantenimiento.setEstado(
                dto.getEstado() != null
                        ? dto.getEstado()
                        : EstadoMantenimiento.PENDIENTE
        );

        return mantenimiento;
    }

    @Override
    public List<MantenimientoDTO> obtenerTodos() {

        return mantenimientoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MantenimientoDTO obtenerPorId(Long id) {

        Mantenimiento mantenimiento = mantenimientoRepository.findById(id)
                .orElseThrow(() ->
                        new MantenimientoNotFoundException(
                                "Mantenimiento no encontrado con id: " + id
                        )
                );

        return toDTO(mantenimiento);
    }

    @Override
    public MantenimientoDTO guardar(MantenimientoDTO mantenimientoDTO) {

        if (mantenimientoDTO.getVehiculoId() == null) {

            throw new MantenimientoVehiculoRequiredException(
                    "El vehiculoId es obligatorio"
            );
        }

        Vehiculo vehiculo = vehiculoRepository.findById(
                        mantenimientoDTO.getVehiculoId()
                )
                .orElseThrow(() ->
                        new VehiculoNotFoundException(
                                "Vehículo no encontrado con id: "
                                        + mantenimientoDTO.getVehiculoId()
                        )
                );

        Mantenimiento mantenimiento = toEntity(mantenimientoDTO);

        mantenimiento.setVehiculo(vehiculo);

        Mantenimiento guardado = mantenimientoRepository.save(mantenimiento);

        return toDTO(guardado);
    }

    @Override
    public MantenimientoDTO actualizar(Long id,
                                       MantenimientoDTO mantenimientoDTO) {

        Mantenimiento existente = mantenimientoRepository.findById(id)
                .orElseThrow(() ->
                        new MantenimientoNotFoundException(
                                "Mantenimiento no encontrado con id: " + id
                        )
                );

        existente.setFecha(mantenimientoDTO.getFecha());
        existente.setDescripcion(mantenimientoDTO.getDescripcion());
        existente.setTipoMantenimiento(
                mantenimientoDTO.getTipoMantenimiento()
        );
        existente.setCosto(mantenimientoDTO.getCosto());

        // Solo actualiza estado si viene en request
        if (mantenimientoDTO.getEstado() != null) {

            existente.setEstado(mantenimientoDTO.getEstado());
        }

        // Validar vehículo existente
        if (mantenimientoDTO.getVehiculoId() != null) {

            Vehiculo vehiculo = vehiculoRepository.findById(
                            mantenimientoDTO.getVehiculoId()
                    )
                    .orElseThrow(() ->
                            new VehiculoNotFoundException(
                                    "Vehículo no encontrado con id: "
                                            + mantenimientoDTO.getVehiculoId()
                            )
                    );

            existente.setVehiculo(vehiculo);
        }

        Mantenimiento actualizado =
                mantenimientoRepository.save(existente);

        return toDTO(actualizado);
    }

    @Override
    public void eliminar(Long id) {

        if (!mantenimientoRepository.existsById(id)) {

            throw new MantenimientoNotFoundException(
                    "Mantenimiento no encontrado con id: " + id
            );
        }

        mantenimientoRepository.deleteById(id);
    }
}