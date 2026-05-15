# LogiFlow - Fase 1

Implementación de la Fase 1 del proyecto con dos microservicios REST:

- `ms-flota`: gestión de vehículos, conductores y consultas de disponibilidad para ruteo.
- `ms-taller`: consulta de vehículos por matrícula y registro de órdenes de mantenimiento.

El alcance de esta entrega está normalizado a REST en ambos servicios. La propuesta DDD se documenta en `docs/ddd-fase1.md`.

## Estructura

```text
Backend/
├── ms-flota
└── ms-taller
```

## Requisitos

- Java 17
- Maven Wrapper (`mvnw` o `mvnw.cmd`)
- PostgreSQL para `ms-flota`
- MySQL para `ms-taller`
- Docker Desktop opcional para las bases de datos

## Ejecutar ms-flota

```powershell
cd Backend/ms-flota
docker-compose up -d
.\mvnw.cmd spring-boot:run
```

- API: `http://localhost:8080`
- Swagger/OpenAPI: `http://localhost:8080/swagger-ui/index.html`

### Endpoints principales ms-flota

- `GET /api/vehiculos`
- `GET /api/vehiculos/{id}`
- `GET /api/vehiculos/matricula/{matricula}`
- `GET /api/vehiculos/disponibles`
- `GET /api/vehiculos/disponibilidad?capacidadMinima=1000`
- `POST /api/vehiculos`
- `PUT /api/vehiculos/{id}`
- `DELETE /api/vehiculos/{id}`
- `GET /api/conductores`
- `GET /api/conductores/{id}`
- `POST /api/conductores`
- `PUT /api/conductores/{id}`
- `DELETE /api/conductores/{id}`

## Ejecutar ms-taller

```powershell
cd Backend/ms-taller
docker-compose up -d
.\mvnw.cmd spring-boot:run
```

- API: `http://localhost:8084`
- Swagger/OpenAPI: `http://localhost:8084/swagger-ui/index.html`

### Endpoints principales ms-taller

- `GET /api/taller/vehiculos/{matricula}`
- `GET /api/taller/consultar-vehiculo/{matricula}`
- `POST /api/taller/ordenes-mantenimiento`
- `POST /api/taller/registrar-orden-mantenimiento`
- `GET /api/taller/ordenes-mantenimiento`
- `GET /api/taller/ordenes-mantenimiento/{id}`
- `GET /api/taller/ordenes-mantenimiento/matricula/{matricula}`
- `GET /api/taller/ordenes-mantenimiento/estado/{estado}`
- `PUT /api/taller/ordenes-mantenimiento/{id}`
- `PUT /api/taller/ordenes-mantenimiento/{id}/estado`
- `DELETE /api/taller/ordenes-mantenimiento/{id}`

## Integración entre servicios

- `ms-taller` consume por REST a `ms-flota` usando `ms-flota.base-url`.
- Valor por defecto: `http://localhost:8080`
- Endpoint consumido: `GET /api/vehiculos/matricula/{matricula}`

## Pipeline DevOps

El workflow principal está en `.github/workflows/logiflow.yml` y contempla:

- ramas `main` y `development`
- ejecución en `push` y `pull_request`
- build y pruebas de `ms-flota`
- build y pruebas de `ms-taller`
- análisis de calidad con SonarCloud
- notificación a Telegram con el estado del pipeline

### Secrets requeridos en GitHub

- `SONAR_TOKEN`
- `TELEGRAM_BOT_TOKEN`
- `TELEGRAM_CHAT_ID`

### Proyecto de Sonar

- `sonar.projectKey=jazambrano21_LogiFlow-Proyecto-Integrador`
- archivo base de análisis: `sonar-project.properties`

## Ramas

La estrategia esperada del repositorio es:

- `development`: integración
- `main`: producción

Para dejar `main` igual a `development`, haz el merge o fast-forward desde tu entorno con Git una vez valides estos cambios.

## Documento DDD

- Propuesta de arquitectura: `docs/ddd-fase1.md`
