# Postman Examples - ms-ruteo-asignacion

## Base URL
http://localhost:8085/api/ruteo

## Swagger UI
http://localhost:8085/swagger-ui/index.html

---

## 1. Crear asignacion
POST http://localhost:8085/api/ruteo/asignar
Content-Type: application/json

Body:
{
  "pedidoId": 1,
  "vehiculoId": 1,
  "conductorId": 1,
  "rutaEstimada": "Quito - Latacunga",
  "kilometros": 89.5
}

Respuesta esperada (201 CREATED):
{
  "id": 1,
  "pedidoId": 1,
  "vehiculoId": 1,
  "conductorId": 1,
  "rutaEstimada": "Quito - Latacunga",
  "kilometros": 89.5,
  "estado": "ASIGNADO",
  "fechaAsignacion": "2026-05-11T17:30:00"
}

---

## 2. Listar envios
GET http://localhost:8085/api/ruteo/envios

---

## 3. Buscar por ID
GET http://localhost:8085/api/ruteo/envios/1

---

## 4. Buscar por pedido
GET http://localhost:8085/api/ruteo/envios/pedido/1

---

## 5. Buscar por vehiculo
GET http://localhost:8085/api/ruteo/envios/vehiculo/1

---

## 6. Buscar por conductor
GET http://localhost:8085/api/ruteo/envios/conductor/1

---

## 7. Actualizar estado
PUT http://localhost:8085/api/ruteo/envios/1/estado
Content-Type: application/json

Body:
{
  "estado": "EN_RUTA"
}

---

## 8. Eliminar envio
DELETE http://localhost:8085/api/ruteo/envios/1

---

## Docker Compose
Para levantar MySQL:
```bash
docker-compose up -d
```

Para verificar contenedor:
```bash
docker ps
```

Para logs:
```bash
docker logs mysql_ruteo
```
