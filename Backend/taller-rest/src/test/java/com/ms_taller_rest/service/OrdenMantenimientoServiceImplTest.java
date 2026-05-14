package com.ms_taller_rest.service;

import com.ms_taller_rest.dto.OrdenMantenimientoRequest;
import com.ms_taller_rest.dto.OrdenMantenimientoResponse;
import com.ms_taller_rest.entity.OrdenMantenimiento;
import com.ms_taller_rest.enums.EstadoMantenimiento;
import com.ms_taller_rest.enums.TipoMantenimiento;
import com.ms_taller_rest.repository.OrdenMantenimientoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrdenMantenimientoServiceImplTest {

    @Mock
    private OrdenMantenimientoRepository ordenMantenimientoRepository;

    @InjectMocks
    private OrdenMantenimientoServiceImpl service;

    @Test
    void registrarOrden_debeCrearOrdenConEstadoPendiente() {
        OrdenMantenimientoRequest request = new OrdenMantenimientoRequest(
                "PBA-1234",
                "Cambio de aceite",
                "Observación",
                TipoMantenimiento.PREVENTIVO,
                85.5
        );

        when(ordenMantenimientoRepository.save(any(OrdenMantenimiento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrdenMantenimientoResponse response = service.registrarOrden(request);

        ArgumentCaptor<OrdenMantenimiento> captor = ArgumentCaptor.forClass(OrdenMantenimiento.class);
        verify(ordenMantenimientoRepository).save(captor.capture());

        OrdenMantenimiento saved = captor.getValue();
        assertEquals("PBA-1234", saved.getMatricula());
        assertEquals("Cambio de aceite", saved.getDescripcion());
        assertEquals(EstadoMantenimiento.PENDIENTE, saved.getEstado());
        assertNotNull(saved.getFechaRegistro());
        assertEquals("PBA-1234", response.getMatricula());
        assertEquals(EstadoMantenimiento.PENDIENTE, response.getEstado());
    }

    @Test
    void actualizarEstado_debeCambiarElEstadoCuandoEsValido() {
        OrdenMantenimiento orden = new OrdenMantenimiento();
        orden.setId(1L);
        orden.setMatricula("PBA-1234");
        orden.setDescripcion("Cambio de aceite");
        orden.setFechaRegistro(LocalDateTime.now());
        orden.setEstado(EstadoMantenimiento.PENDIENTE);
        orden.setTipoMantenimiento(TipoMantenimiento.PREVENTIVO);

        when(ordenMantenimientoRepository.findById(1L)).thenReturn(Optional.of(orden));
        when(ordenMantenimientoRepository.save(any(OrdenMantenimiento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrdenMantenimientoResponse response = service.actualizarEstado(1L, "finalizado");

        assertEquals(EstadoMantenimiento.FINALIZADO, response.getEstado());
    }

    @Test
    void obtenerPorEstado_conValorInvalido_debeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> service.obtenerPorEstado("NO_EXISTE"));
    }
}

