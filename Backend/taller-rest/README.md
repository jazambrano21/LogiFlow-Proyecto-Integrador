# ms-taller-rest

Microservicio REST para gestión de órdenes de mantenimiento de vehículos - LogiFlow.

## Tecnologías Utilizadas

- Java 17
- Spring Boot 3.2.0
- Spring Web
- Spring Data JPA
- MySQL 8.0
- Lombok
- Jakarta Validation
- SpringDoc OpenAPI (Swagger)
- Maven

## Configuración del Microservicio

- **Nombre**: ms-taller-rest
- **Puerto**: 8084
- **Base de datos**: logiflow_taller
- **MySQL puerto externo**: 3309
- **MySQL puerto interno**: 3306

## Configuración de la Base de Datos

El archivo `application.properties` está configurado para conectarse a MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3309/logiflow_taller?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
```

## Ejecutar la Aplicación

### Opción 1: Ejecución Local

Primero, inicia MySQL con Docker Compose:

```bash
cd taller-rest
docker-compose up -d
```

Luego ejecuta la aplicación con Maven:

```bash
mvn spring-boot:run
```

O construye el JAR y ejecútalo:

```bash
mvn clean package
java -jar target/taller-rest-1.0.0.jar
```

### Opción 2: Ejecución con Docker

```bash
# Iniciar MySQL
docker-compose up -d

# Construir y ejecutar la aplicación
docker build -t ms-taller-rest:1.0.0 .
docker run -p 8084:8084 ms-taller-rest:1.0.0
```

## Documentación Swagger

Una vez que la aplicación esté ejecutándose, accede a la documentación Swagger en:

```
http://localhost:8084/swagger-ui/index.html
```

## Estructura del Proyecto

```
com.ms_taller_rest
│
├── controller
│   └── OrdenMantenimientoController.java
│
├── dto
│   ├── OrdenMantenimientoRequest.java
│   ├── OrdenMantenimientoResponse.java
│   ├── EstadoMantenimientoRequest.java
│   └── VehiculoConsultaResponse.java
│
├── entity
│   └── OrdenMantenimiento.java
│
├── enums
│   ├── EstadoMantenimiento.java
│   └── TipoMantenimiento.java
│
├── repository
│   └── OrdenMantenimientoRepository.java
│
├── service
│   ├── OrdenMantenimientoService.java
│   └── OrdenMantenimientoServiceImpl.java
│
└── TallerRestApplication.java
```

## Ejemplos de Pruebas con Postman

### 1. Registrar orden de mantenimiento

**Endpoint**: `POST http://localhost:8084/api/taller/ordenes-mantenimiento`

**Body**:
```json
{
  "matricula": "PBA-1234",
  "descripcion": "Cambio de aceite y revisión de frenos",
  "observacion": "Vehículo reportado por el conductor",
  "tipoMantenimiento": "PREVENTIVO",
  "costoEstimado": 85.50
}
```

**Response**:
```json
{
  "id": 1,
  "matricula": "PBA-1234",
  "descripcion": "Cambio de aceite y revisión de frenos",
  "fechaRegistro": "2024-01-15T10:30:00",
  "estado": "PENDIENTE",
  "observacion": "Vehículo reportado por el conductor",
  "tipoMantenimiento": "PREVENTIVO",
  "costoEstimado": 85.50
}
```

### 2. Listar todas las órdenes

**Endpoint**: `GET http://localhost:8084/api/taller/ordenes-mantenimiento`

**Response**:
```json
[
  {
    "id": 1,
    "matricula": "PBA-1234",
    "descripcion": "Cambio de aceite y revisión de frenos",
    "fechaRegistro": "2024-01-15T10:30:00",
    "estado": "PENDIENTE",
    "observacion": "Vehículo reportado por el conductor",
    "tipoMantenimiento": "PREVENTIVO",
    "costoEstimado": 85.50
  }
]
```

### 3. Buscar orden por ID

**Endpoint**: `GET http://localhost:8084/api/taller/ordenes-mantenimiento/1`

**Response**:
```json
{
  "id": 1,
  "matricula": "PBA-1234",
  "descripcion": "Cambio de aceite y revisión de frenos",
  "fechaRegistro": "2024-01-15T10:30:00",
  "estado": "PENDIENTE",
  "observacion": "Vehículo reportado por el conductor",
  "tipoMantenimiento": "PREVENTIVO",
  "costoEstimado": 85.50
}
```

### 4. Buscar órdenes por matrícula

**Endpoint**: `GET http://localhost:8084/api/taller/ordenes-mantenimiento/matricula/PBA-1234`

**Response**:
```json
[
  {
    "id": 1,
    "matricula": "PBA-1234",
    "descripcion": "Cambio de aceite y revisión de frenos",
    "fechaRegistro": "2024-01-15T10:30:00",
    "estado": "PENDIENTE",
    "observacion": "Vehículo reportado por el conductor",
    "tipoMantenimiento": "PREVENTIVO",
    "costoEstimado": 85.50
  }
]
```

### 5. Buscar órdenes por estado

**Endpoint**: `GET http://localhost:8084/api/taller/ordenes-mantenimiento/estado/PENDIENTE`

**Response**:
```json
[
  {
    "id": 1,
    "matricula": "PBA-1234",
    "descripcion": "Cambio de aceite y revisión de frenos",
    "fechaRegistro": "2024-01-15T10:30:00",
    "estado": "PENDIENTE",
    "observacion": "Vehículo reportado por el conductor",
    "tipoMantenimiento": "PREVENTIVO",
    "costoEstimado": 85.50
  }
]
```

### 6. Actualizar orden

**Endpoint**: `PUT http://localhost:8084/api/taller/ordenes-mantenimiento/1`

**Body**:
```json
{
  "matricula": "PBA-1234",
  "descripcion": "Cambio de aceite, revisión de frenos y alineación",
  "observacion": "Se amplía el mantenimiento solicitado",
  "tipoMantenimiento": "CORRECTIVO",
  "costoEstimado": 120.00
}
```

**Response**:
```json
{
  "id": 1,
  "matricula": "PBA-1234",
  "descripcion": "Cambio de aceite, revisión de frenos y alineación",
  "fechaRegistro": "2024-01-15T10:30:00",
  "estado": "PENDIENTE",
  "observacion": "Se amplía el mantenimiento solicitado",
  "tipoMantenimiento": "CORRECTIVO",
  "costoEstimado": 120.00
}
```

### 7. Actualizar estado

**Endpoint**: `PUT http://localhost:8084/api/taller/ordenes-mantenimiento/1/estado`

**Body**:
```json
{
  "estado": "EN_PROCESO"
}
```

**Estados disponibles**:
- `PENDIENTE`
- `EN_PROCESO`
- `FINALIZADO`
- `CANCELADO`

**Response**:
```json
{
  "id": 1,
  "matricula": "PBA-1234",
  "descripcion": "Cambio de aceite, revisión de frenos y alineación",
  "fechaRegistro": "2024-01-15T10:30:00",
  "estado": "EN_PROCESO",
  "observacion": "Se amplía el mantenimiento solicitado",
  "tipoMantenimiento": "CORRECTIVO",
  "costoEstimado": 120.00
}
```

### 8. Consultar vehículo por matrícula

**Endpoint**: `GET http://localhost:8084/api/taller/vehiculos/PBA-1234`

**Response**:
```json
{
  "matricula": "PBA-1234",
  "marca": "Hino",
  "modelo": "XZU",
  "tipo": "CAMION",
  "estado": "DISPONIBLE",
  "mensaje": "Vehículo consultado correctamente desde ms-taller-rest"
}
```

### 9. Eliminar orden

**Endpoint**: `DELETE http://localhost:8084/api/taller/ordenes-mantenimiento/1`

**Response**: `204 No Content`

## Estados de Mantenimiento

- **PENDIENTE**: Orden creada y esperando ser procesada
- **EN_PROCESO**: Orden siendo procesada actualmente
- **FINALIZADO**: Orden completada exitosamente
- **CANCELADO**: Orden cancelada

## Tipos de Mantenimiento

- **PREVENTIVO**: Mantenimiento programado para prevenir fallos
- **CORRECTIVO**: Mantenimiento para corregir un fallo existente
- **EMERGENCIA**: Mantenimiento de urgencia

## Reglas de Negocio

- Toda orden nueva inicia automáticamente con estado PENDIENTE
- La fecha de registro se genera automáticamente con LocalDateTime.now()
- La consulta de vehículo por matrícula es simulada (datos de prueba)
- Los DTOs evitan exponer entidades directamente
- Validaciones con Jakarta Validation en todos los requests

## Validaciones

**OrdenMantenimientoRequest**:
- matricula: Obligatorio
- descripcion: Obligatorio
- tipoMantenimiento: Obligatorio
- observacion: Opcional
- costoEstimado: Opcional

**EstadoMantenimientoRequest**:
- estado: Obligatorio

## Arquitectura

El microservicio sigue buenas prácticas de arquitectura REST:

- Separación de capas (Controller, Service, Repository)
- Uso de DTOs para transferencia de datos
- Inyección de dependencias por constructor
- Validaciones con Jakarta Validation
- Documentación automática con Swagger/OpenAPI
- Transacciones con Spring @Transactional
- Dockerización para fácil despliegue

## Comandos Docker Útiles

```bash
# Iniciar MySQL
docker-compose up -d

# Ver logs de MySQL
docker-compose logs -f mysql-taller

# Detener MySQL
docker-compose down

# Detener MySQL y eliminar datos
docker-compose down -v

# Ver estado de los contenedores
docker-compose ps
```
