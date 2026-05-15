# ms-flota

Microservicio REST para la gestión de vehículos, conductores y consultas de disponibilidad para ruteo.

## Ejecución local

```powershell
docker-compose up -d
.\mvnw.cmd spring-boot:run
```

## Configuración

- Puerto: `8080`
- Nombre de aplicación: `ms-flota`
- Swagger: `http://localhost:8080/swagger-ui/index.html`

## Endpoints principales

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
