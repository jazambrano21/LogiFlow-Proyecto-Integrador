# ms-pedidos

Microservicio REST de gestión de pedidos con mensajería RabbitMQ para LogiFlow Fase 2.

## Puerto

- REST API: `8083`
- Swagger/OpenAPI: `http://localhost:8083/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8083/api-docs`

## Endpoints

Todas las rutas requieren `Authorization: Bearer <token>`.

| Método | Ruta                  | Descripción                                            |
|--------|-----------------------|--------------------------------------------------------|
| POST   | /pedidos              | Crear pedido → publica `pedido.creado` a RabbitMQ      |
| GET    | /pedidos              | Listar (`?estado=CREADO&clienteId=1`)                  |
| GET    | /pedidos/{id}         | Detalle del pedido                                     |
| PUT    | /pedidos/{id}/estado  | Actualizar estado según máquina de estados             |
| DELETE | /pedidos/{id}         | Cancelar → publica `pedido.cancelado` a RabbitMQ       |

## Máquina de estados

```
CREADO → ASIGNADO → EN_RUTA → ENTREGADO
CREADO → CANCELADO
ASIGNADO → CANCELADO
```

## Ejemplo crear pedido

```json
POST /pedidos
Authorization: Bearer <token>
{
  "clienteId": 1,
  "origen": { "ciudad": "Quito", "direccion": "Av. 6 de Diciembre N34-20" },
  "destino": { "ciudad": "Guayaquil", "direccion": "Av. 9 de Octubre 100" },
  "paquete": { "pesoKg": 5.5, "descripcion": "Electrónicos frágiles" },
  "prioridad": "URGENTE",
  "nivelGeografico": "NACIONAL"
}
```

## Eventos publicados a RabbitMQ

| Evento             | Routing key         | Cuándo                        |
|-------------------|---------------------|-------------------------------|
| `pedido.creado`    | `pedido.creado`     | Al crear un pedido            |
| `pedido.cancelado` | `pedido.cancelado`  | Al cancelar (DELETE /pedidos/{id}) |

Exchange: `logistica.topic`

## Variables de entorno

| Variable             | Por defecto                                           |
|---------------------|-------------------------------------------------------|
| `PORT`              | `8083`                                                |
| `DB_URL`            | `jdbc:mysql://localhost:3312/ms_pedidos_db?...`       |
| `DB_USERNAME`       | `root`                                                |
| `DB_PASSWORD`       | `root`                                                |
| `JWT_SECRET`        | `logiflow-secret-key-fase2-must-be-at-least-32-chars` |
| `RABBITMQ_HOST`     | `localhost`                                           |
| `RABBITMQ_PORT`     | `5672`                                                |
| `RABBITMQ_USERNAME` | `logiflow`                                            |
| `RABBITMQ_PASSWORD` | `logiflow2026`                                        |

## Ejecución local

```powershell
cd Backend/ms-pedidos
docker-compose up -d        # MySQL en 3312 + RabbitMQ en 5672
.\mvnw.cmd spring-boot:run
```
