# ms-seguimiento

Microservicio de seguimiento en tiempo real para LogiFlow.

**No expone API REST.** Funciona exclusivamente con WebSocket (Socket.IO) y consume mensajes de RabbitMQ.

## Puerto

- WebSocket: `3005` (configurable con `WS_PORT`)

## Protocolo WebSocket

Conectar al servidor con cualquier cliente Socket.IO:

```js
import { io } from 'socket.io-client';
const socket = io('http://localhost:3005');
```

### Evento de cliente → servidor: `subscribe`

Suscribirse a actualizaciones de posición de un envío:

```json
{ "envioId": 42 }
```

El servidor responde con `subscribed`:

```json
{ "envioId": "42", "message": "Suscrito a envioId 42" }
```

### Evento de servidor → cliente: `posicion`

Recibido automáticamente cuando RabbitMQ entrega una posición actualizada:

```json
{
  "envioId": 42,
  "lat": -0.1807,
  "lng": -78.4678,
  "velocidad": 75,
  "eta": "2026-06-21T14:30:00.000Z",
  "timestamp": "2026-06-21T12:00:00.000Z"
}
```

## Variables de entorno

| Variable           | Por defecto                                    | Descripción                      |
|-------------------|------------------------------------------------|----------------------------------|
| `WS_PORT`         | `3005`                                         | Puerto del servidor WebSocket    |
| `RABBITMQ_URL`    | `amqp://logiflow:logiflow2026@localhost:5672`  | URL de conexión a RabbitMQ       |
| `RABBITMQ_QUEUE`  | `q.seguimiento.posicion`                       | Cola que consume                 |
| `RABBITMQ_EXCHANGE` | `logistica.topic`                            | Exchange de LogiFlow             |

## Ejecución local

```powershell
# 1. Instalar dependencias
cd Backend/ms-seguimiento
npm install

# 2. Configurar entorno
cp .env.example .env   # ajustar RABBITMQ_URL si es necesario

# 3. Iniciar el servicio
npm start

# 4. Modo desarrollo con recarga automática
npm run dev
```

## Simulador de posiciones

Para pruebas sin un conductor real, el simulador publica una posición cada 3 segundos a RabbitMQ:

```powershell
# Simulación básica (envioId=1)
npm run simulate

# Simulación para un envío específico
npm run simulate -- --envioId=5

# Simulación con cantidad limitada de mensajes
npm run simulate -- --envioId=5 --count=20
```

El simulador traza una ruta aproximada Quito → Guayaquil con variación aleatoria de coordenadas.

## Ejecución con Docker

```powershell
# Desde la raíz del proyecto
docker-compose up -d ms-seguimiento
```

## Cola RabbitMQ consumida

| Cola                      | Routing key          | Descripción                    |
|--------------------------|----------------------|-------------------------------|
| `q.seguimiento.posicion`  | `posicion.actualizada` | Actualizaciones GPS en tiempo real |
