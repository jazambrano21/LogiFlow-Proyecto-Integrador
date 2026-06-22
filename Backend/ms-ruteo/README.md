# ms-ruteo

Microservicio de ruteo y asignación de envíos para LogiFlow Fase 2.

## Puerto

- REST API: `8085`
- Swagger/OpenAPI: `http://localhost:8085/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8085/api-docs`

## Funcionalidad

### Asignación automática (RabbitMQ)

Consume la cola `q.ruteo.pedido-creado`. Al recibir un evento `pedido.creado`:

1. Consulta vehículos disponibles en `ms-flota` (`GET /api/vehiculos/disponibles?capacidadMinima=X`)
2. Aplica el algoritmo de selección según nivel geográfico:
   - `LOCAL` → prefiere MOTO o AUTO
   - `PROVINCIAL` → prefiere FURGONETA
   - `NACIONAL` → prefiere CAMION
3. Persiste el `Envio` en base de datos
4. Publica evento `envio.asignado` al exchange `logistica.topic`

### Asignación manual (REST)

`POST /envios/asignar-manual` — el operador elige directamente vehículo y conductor.

## Endpoints REST

| Método | Ruta                    | Descripción                                          |
|--------|------------------------|------------------------------------------------------|
| GET    | /envios                 | Listar envíos (`?estado=&pedidoId=&vehiculoId=`)     |
| GET    | /envios/{id}            | Detalle con ruta completa                            |
| PUT    | /envios/{id}/estado     | Actualizar estado (ASIGNADO→EN_RUTA→ENTREGADO)       |
| POST   | /envios/asignar-manual  | Asignación manual sin algoritmo                      |

Al actualizar estado a `ENTREGADO`, publica evento `pedido.entregado` a RabbitMQ.

Todas las rutas requieren `Authorization: Bearer <token>`.

## Variables de entorno

| Variable         | Por defecto                                              |
|-----------------|----------------------------------------------------------|
| `PORT`          | `8085`                                                   |
| `DB_URL`        | `jdbc:mysql://localhost:3313/ms_ruteo_db?...`            |
| `DB_USERNAME`   | `root`                                                   |
| `DB_PASSWORD`   | `root`                                                   |
| `JWT_SECRET`    | `logiflow-secret-key-fase2-must-be-at-least-32-chars`    |
| `RABBITMQ_HOST` | `localhost`                                              |
| `RABBITMQ_PORT` | `5672`                                                   |
| `RABBITMQ_USERNAME` | `logiflow`                                           |
| `RABBITMQ_PASSWORD` | `logiflow2026`                                       |
| `MS_FLOTA_URL`  | `http://localhost:8080`                                  |

## Ejecución local

```powershell
cd Backend/ms-ruteo
docker-compose up -d        # levanta mysql-ruteo en puerto 3313
.\mvnw.cmd spring-boot:run
```

## Ejecución con Docker Compose (raíz)

```powershell
docker-compose up -d ms-ruteo
```

## Eventos RabbitMQ

| Dirección  | Routing key       | Cola / Exchange              | Descripción              |
|-----------|-------------------|------------------------------|--------------------------|
| Consume   | `pedido.creado`   | `q.ruteo.pedido-creado`      | Dispara asignación auto  |
| Publica   | `envio.asignado`  | `logistica.topic`            | Notifica asignación OK   |
| Publica   | `pedido.entregado`| `logistica.topic`            | Notifica entrega final   |
