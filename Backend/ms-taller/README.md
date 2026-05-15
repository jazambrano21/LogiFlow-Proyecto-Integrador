# ms-taller

Microservicio REST para registrar órdenes de mantenimiento y consultar datos del vehículo a través de `ms-flota`.

## Ejecución local

```powershell
docker-compose up -d
.\mvnw.cmd spring-boot:run
```

## Configuración

- Puerto: `8084`
- Nombre de aplicación: `ms-taller`
- Base de datos: `logiflow_taller`
- Dependencia REST: `ms-flota.base-url=http://localhost:8080`
- Swagger: `http://localhost:8084/swagger-ui/index.html`

## Endpoints principales

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

## Ejemplo de registro

```json
{
  "matricula": "PBA-1234",
  "descripcion": "Cambio de aceite y revisión de frenos",
  "observacion": "Vehículo reportado por el conductor",
  "tipoMantenimiento": "PREVENTIVO",
  "costoEstimado": 85.50
}
```

## Reglas principales

- Toda orden nueva inicia en estado `PENDIENTE`.
- La fecha de registro se genera automáticamente.
- La consulta de vehículo se resuelve consumiendo el endpoint REST de `ms-flota`.
