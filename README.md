# LogiFlow - Proyecto Integrador
# LogiFlow

Microservicio REST para gestionar flotas vehiculares. Administra conductores, vehículos y mantenimientos de una empresa de transporte/logística. Permite registrar conductores (con estados: disponible, en tarea, inactivo), vehículos (con estados: disponible, en uso, en mantenimiento, fuera de servicio) y controlar los mantenimientos de cada vehículo (pendiente, en proceso, completado, cancelado).

## Ejecutar
```powershell
docker-compose up -d
```

API: `http://localhost:8080`  
Swagger: `http://localhost:8080/swagger-ui.html`

## Endpoints

**Conductores** `/api/conductores`
- GET / GET {id} / POST / PUT {id} / DELETE {id}

**Vehículos** `/api/vehiculos`
- GET / GET {id} / GET /disponibles / POST / PUT {id} / DELETE {id}

**Mantenimientos** `/api/mantenimientos`
- GET / GET {id} / POST / PUT {id} / DELETE {id}

## Parar
```powershell
docker-compose down -v
```
 
## Microservicio Taller-rest
# ms-taller-rest
Microservicio REST para gestionar órdenes de mantenimiento de vehículos en LogiFlow.
## Tecnologías
- Java 17
- Spring Boot 3.2
- Spring Data JPA
- MySQL
- Maven
- Swagger / OpenAPI
## Configuración
- Puerto: `8084`
- Base de datos: `logiflow_taller`
- MySQL: `localhost:3309`
## Ejecutar el proyecto
1. Levantar MySQL con Docker:
```bash
docker-compose up -d
## Swagger 
URL: http://localhost:8084/swagger-ui/index.html
Funcionalidad principal
Crear órdenes de mantenimiento
Consultar órdenes por ID, matrícula o estado
Actualizar órdenes y su estado
Eliminar órdenes
Consultar vehículos
Registrar incidencias de mantenimiento

Endpoints principales
POST /api/taller/ordenes-mantenimiento
GET /api/taller/ordenes-mantenimiento
GET /api/taller/ordenes-mantenimiento/{id}
PUT /api/taller/ordenes-mantenimiento/{id}
PUT /api/taller/ordenes-mantenimiento/{id}/estado
DELETE /api/taller/ordenes-mantenimiento/{id}
GET /api/taller/vehiculos/{matricula}
POST /api/taller/ordenes-mantenimiento/{id}/incidencias


## Configuración del pipeline

Este repositorio usa una GitHub Action definida en `.github/workflows/build.yml`.

La configuración del pipeline incluye:
- Desencadenadores en `push` y `pull_request` para las ramas `main` y `development`.
- Preparación del entorno con JDK 17 usando `actions/setup-java@v4`.
- Caché para dependencias Maven y archivos de SonarCloud.
- Ejecución de `mvn -B verify sonar:sonar` dentro de `Backend/ms-autenticacion`.
- Análisis de calidad en SonarCloud con el proyecto `jazambrano21_LogiFlow-Proyecto-Integrador`.
- Notificaciones por Telegram cuando la compilación termina en éxito o fallo.

Esta configuración permite verificar el código, ejecutar pruebas y controlar la calidad en cada cambio relevante del proyecto.


*Prueba de CI: cambio menor.*
