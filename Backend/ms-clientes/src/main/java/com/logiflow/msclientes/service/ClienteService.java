package com.logiflow.msclientes.service;

import com.logiflow.msclientes.dto.*;
import com.logiflow.msclientes.entity.Cliente;
import com.logiflow.msclientes.entity.CuentaCorporativa;
import com.logiflow.msclientes.entity.TipoCliente;
import com.logiflow.msclientes.exception.ClienteNotFoundException;
import com.logiflow.msclientes.exception.ConflictException;
import com.logiflow.msclientes.repository.ClienteRepository;
import com.logiflow.msclientes.repository.CuentaCorporativaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final CuentaCorporativaRepository cuentaRepository;

    // ──────────────── CLIENTES ────────────────

    public List<ClienteResponse> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ClienteResponse obtenerPorId(Long id) {
        return toResponse(findCliente(id));
    }

    @Transactional
    public ClienteResponse crear(ClienteRequest request) {
        if (clienteRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("El email '" + request.getEmail() + "' ya está registrado");
        }
        Cliente cliente = Cliente.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .tipo(request.getTipo())
                .build();
        return toResponse(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteResponse actualizar(Long id, ClienteRequest request) {
        Cliente cliente = findCliente(id);
        if (!cliente.getEmail().equals(request.getEmail()) &&
                clienteRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("El email '" + request.getEmail() + "' ya está en uso");
        }
        cliente.setNombre(request.getNombre());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());
        cliente.setTipo(request.getTipo());
        return toResponse(clienteRepository.save(cliente));
    }

    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = findCliente(id);
        cuentaRepository.findByClienteId(id).ifPresent(cuentaRepository::delete);
        clienteRepository.delete(cliente);
    }

    // ──────────────── CUENTA CORPORATIVA ────────────────

    @Transactional
    public CuentaCorporativaResponse crearCuenta(Long clienteId, CuentaCorporativaRequest request) {
        Cliente cliente = findCliente(clienteId);

        if (cliente.getTipo() != TipoCliente.CORPORATIVO) {
            throw new ConflictException("Solo clientes de tipo CORPORATIVO pueden tener cuenta corporativa");
        }
        if (cuentaRepository.existsByClienteId(clienteId)) {
            throw new ConflictException("El cliente ya tiene una cuenta corporativa");
        }
        if (cuentaRepository.existsByRuc(request.getRuc())) {
            throw new ConflictException("El RUC '" + request.getRuc() + "' ya está registrado");
        }

        CuentaCorporativa cuenta = CuentaCorporativa.builder()
                .cliente(cliente)
                .nombreEmpresa(request.getNombreEmpresa())
                .ruc(request.getRuc())
                .saldo(request.getSaldo())
                .contrato(request.getContrato())
                .build();

        return toCuentaResponse(cuentaRepository.save(cuenta));
    }

    @Transactional
    public CuentaCorporativaResponse actualizarCuenta(Long clienteId, CuentaCorporativaRequest request) {
        findCliente(clienteId);
        CuentaCorporativa cuenta = cuentaRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new ClienteNotFoundException(clienteId));

        if (!cuenta.getRuc().equals(request.getRuc()) &&
                cuentaRepository.existsByRuc(request.getRuc())) {
            throw new ConflictException("El RUC '" + request.getRuc() + "' ya está en uso");
        }

        cuenta.setNombreEmpresa(request.getNombreEmpresa());
        cuenta.setRuc(request.getRuc());
        cuenta.setSaldo(request.getSaldo());
        cuenta.setContrato(request.getContrato());
        return toCuentaResponse(cuentaRepository.save(cuenta));
    }

    public CuentaCorporativaResponse obtenerCuenta(Long clienteId) {
        findCliente(clienteId);
        return cuentaRepository.findByClienteId(clienteId)
                .map(this::toCuentaResponse)
                .orElseThrow(() -> new ClienteNotFoundException(clienteId));
    }

    // ──────────────── MAPPERS ────────────────

    private Cliente findCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));
    }

    private ClienteResponse toResponse(Cliente c) {
        return ClienteResponse.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .email(c.getEmail())
                .telefono(c.getTelefono())
                .tipo(c.getTipo())
                .fechaRegistro(c.getFechaRegistro())
                .build();
    }

    private CuentaCorporativaResponse toCuentaResponse(CuentaCorporativa cc) {
        return CuentaCorporativaResponse.builder()
                .id(cc.getId())
                .clienteId(cc.getCliente().getId())
                .nombreEmpresa(cc.getNombreEmpresa())
                .ruc(cc.getRuc())
                .saldo(cc.getSaldo())
                .contrato(cc.getContrato())
                .build();
    }
}
