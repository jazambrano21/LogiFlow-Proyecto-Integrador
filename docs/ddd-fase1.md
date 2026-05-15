# Propuesta DDD - LogiFlow Fase 1

## 1. Objetivo

Definir la propuesta de arquitectura para la Fase 1 mediante descubrimiento del dominio, delimitación de contextos y construcción de dos pilotos REST:

- `ms-flota`
- `ms-taller`

La solución final de esta entrega se normaliza a integración REST-REST.

## 2. Event Storming

### Eventos de dominio

- Vehículo registrado
- Vehículo actualizado
- Vehículo marcado como disponible
- Vehículo marcado como en uso
- Vehículo marcado como en mantenimiento
- Vehículo fuera de servicio
- Conductor registrado
- Conductor actualizado
- Conductor desasignado
- Conductor asignado a vehículo
- Disponibilidad de flota consultada
- Vehículo consultado por matrícula
- Orden de mantenimiento registrada
- Orden de mantenimiento actualizada
- Estado de orden de mantenimiento actualizado
- Orden de mantenimiento cancelada

### Comandos

- Registrar vehículo
- Actualizar vehículo
- Eliminar vehículo
- Registrar conductor
- Actualizar conductor
- Eliminar conductor
- Asignar conductor a vehículo
- Consultar disponibilidad de flota
- Consultar vehículo por matrícula
- Registrar orden de mantenimiento
- Actualizar orden de mantenimiento
- Cambiar estado de orden
- Eliminar orden de mantenimiento

### Agregados

- `Vehiculo`
- `Conductor`
- `OrdenMantenimiento`

## 3. Dominios y Subdominios

### Dominio core

- Operación de flota
- Planeación operativa de rutas y disponibilidad

### Dominios de soporte

- Taller y mantenimiento
- Seguridad y autenticación
- Seguimiento
- Facturación

### Dominos genéricos

- CI/CD
- Observabilidad
- Gestión documental y reportes

## 4. Bounded Contexts

### Contexto Flota

Responsabilidades:

- administrar vehículos
- administrar conductores
- gestionar asignación conductor-vehículo
- exponer disponibilidad para ruteo
- exponer consulta de vehículo por matrícula

Agregado principal:

- `Vehiculo`

Agregado de apoyo:

- `Conductor`

Servicio resultante:

- `ms-flota`

### Contexto Taller

Responsabilidades:

- registrar órdenes de mantenimiento
- consultar órdenes por ID, matrícula y estado
- consultar vehículo por matrícula en formato requerido por taller
- depender del contexto Flota como fuente de verdad del vehículo

Agregado principal:

- `OrdenMantenimiento`

Servicio resultante:

- `ms-taller`

### Contexto Ruteo y Asignación

Responsabilidades:

- consumir disponibilidad publicada por Flota
- planificar vehículos adecuados para una ruta

Patrón actual:

- consumidor downstream del contexto Flota

### Contexto Seguimiento

Responsabilidades:

- registrar posiciones y estados de seguimiento

### Contexto Facturación

Responsabilidades:

- generar y actualizar facturas del proceso logístico

### Contexto Autenticación

Responsabilidades:

- autenticación
- emisión y validación de tokens
- gestión de usuarios

## 5. Lenguaje Ubicuo

### Flota

- Vehículo: unidad operativa disponible para transporte
- Matrícula: identificador único del vehículo
- Capacidad: volumen o carga soportada por el vehículo
- Estado del vehículo: disponibilidad operativa del activo
- Conductor: recurso humano asignable a un vehículo
- Disponibilidad: condición de poder ser asignado a una ruta

### Taller

- Orden de mantenimiento: registro formal de una incidencia o intervención
- Incidencia: descripción del problema reportado
- Estado de orden: ciclo de vida de la orden
- Mantenimiento preventivo: intervención programada
- Mantenimiento correctivo: intervención por falla

### Ruteo

- Consulta de disponibilidad: solicitud para conocer activos utilizables
- Asignación: vinculación de un vehículo a una operación

## 6. Context Map

### `Flota` -> `Taller`

- Patrón: `Customer/Supplier`
- Razón: Taller consume la información maestra del vehículo administrada por Flota.

### `Taller` -> `Flota`

- Patrón: `Conformist`
- Razón: Taller se adapta al modelo expuesto por Flota para la consulta por matrícula.

### `Ruteo` -> `Flota`

- Patrón: `Open Host Service`
- Razón: Flota expone un contrato REST público para disponibilidad y consulta de vehículos.

### `Ruteo` -> `Flota`

- Patrón: `Published Language`
- Razón: DTOs REST publicados por Flota definen el lenguaje de integración.

### `Taller` -> `Flota`

- Patrón: `Anticorruption Layer`
- Razón: Taller transforma la respuesta de Flota al formato de consulta requerido por el taller externo.

### `Autenticación` <-> `Flota`, `Taller`, `Seguimiento`, `Facturación`

- Patrón: `Partnership`
- Razón: seguridad transversal con acuerdos compartidos de acceso e identidad.

### `Seguimiento` -> `Ruteo`

- Patrón: `Customer/Supplier`
- Razón: Seguimiento depende del plan operativo generado por Ruteo.

### `Facturación` -> `Seguimiento`

- Patrón: `Customer/Supplier`
- Razón: Facturación usa estados de entrega y cumplimiento.

## 7. Decisiones Arquitectónicas

- Los dos pilotos exigidos en esta fase quedan implementados como servicios REST independientes.
- `ms-flota` no expone mantenimiento como API pública.
- `ms-taller` concentra la lógica de órdenes de mantenimiento.
- La integración entre ambos servicios se resuelve con HTTP REST.
- OpenAPI es el contrato de integración visible en esta entrega.

## 8. Casos de Uso Cubiertos

- CRUD de vehículos
- CRUD de conductores
- Consulta de disponibilidad para ruteo
- Consulta de vehículo por matrícula
- Registro de orden de mantenimiento
- Consulta de órdenes por ID, matrícula y estado
- Actualización de órdenes y cambio de estado
- Eliminación de órdenes

## 9. Riesgos y Siguientes Pasos

- Formalizar autenticación sobre `ms-flota` y `ms-taller`
- Externalizar configuración por perfiles
- Incorporar trazabilidad distribuida
- Definir eventos asincrónicos para fases posteriores
