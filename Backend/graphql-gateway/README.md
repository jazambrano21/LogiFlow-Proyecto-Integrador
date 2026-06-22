# graphql-gateway

BFF (Backend For Frontend) GraphQL — punto de entrada único para todos los clientes de LogiFlow.

Agrega datos de: ms-auth, ms-clientes, ms-pedidos, ms-flota, ms-ruteo, ms-seguimiento.

## Puerto

- GraphQL (HTTP + WS): `4000`
- Playground/Sandbox: `http://localhost:4000/graphql`
- Health check: `http://localhost:4000/health`
- WebSocket (subscriptions): `ws://localhost:4000/graphql`

## Autenticación

Todas las queries y mutations requieren el token obtenido de ms-auth:

```
Authorization: Bearer <token>
```

Las subscriptions envían el token en los `connectionParams`:

```js
const wsClient = createClient({
  url: 'ws://localhost:4000/graphql',
  connectionParams: {
    authorization: 'Bearer <token>',
  },
});
```

## Queries disponibles

### pedidosActivos

```graphql
query {
  pedidosActivos(clienteId: "1") {
    id
    estado
    prioridad
    nivelGeografico
    fechaCreacion
    cliente {
      nombre
      email
    }
    origen { ciudad direccion }
    destino { ciudad direccion }
    paquete { pesoKg descripcion }
  }
}
```

### pedido

```graphql
query {
  pedido(id: "3") {
    id
    estado
    cliente { nombre }
    paquete { pesoKg descripcion }
  }
}
```

### envio

```graphql
query {
  envio(id: "1") {
    id
    estado
    vehiculoId
    conductorId
    ruta {
      origen { ciudad }
      destino { ciudad }
      kmsEstimados
    }
    horarioEstimado
    ultimaPosicion {
      lat lng velocidad eta timestamp
    }
    pedido {
      id estado prioridad
    }
  }
}
```

### clientes

```graphql
query {
  clientes {
    id nombre email tipo
  }
}
```

### vehiculosDisponibles

```graphql
query {
  vehiculosDisponibles(nivelGeografico: "NACIONAL") {
    id matricula tipo capacidadKg estado
  }
}
```

## Mutations

### crearPedido

```graphql
mutation {
  crearPedido(input: {
    clienteId: "1"
    origen: { ciudad: "Quito", direccion: "Av. 6 de Diciembre N34-20" }
    destino: { ciudad: "Guayaquil", direccion: "Av. 9 de Octubre 100" }
    paquete: { pesoKg: 5.5, descripcion: "Electrónicos frágiles" }
    prioridad: "URGENTE"
    nivelGeografico: "NACIONAL"
  }) {
    id estado fechaCreacion
  }
}
```

### cancelarPedido

```graphql
mutation {
  cancelarPedido(id: "5") {
    id estado
  }
}
```

### actualizarEstadoEnvio

```graphql
mutation {
  actualizarEstadoEnvio(id: "2", estado: "EN_RUTA") {
    id estado horarioEstimado
  }
}
```

## Subscriptions

```graphql
subscription {
  posicionEnvio(envioId: "1") {
    envioId
    lat
    lng
    velocidad
    eta
    timestamp
  }
}
```

Ejemplo con cliente JavaScript (`graphql-ws`):

```js
import { createClient } from 'graphql-ws';

const client = createClient({
  url: 'ws://localhost:4000/graphql',
  connectionParams: { authorization: 'Bearer <token>' },
});

client.subscribe(
  {
    query: `subscription { posicionEnvio(envioId: "1") {
      lat lng velocidad eta timestamp
    }}`,
  },
  {
    next: (data) => console.log('Posición:', data),
    error: (err) => console.error(err),
    complete: () => console.log('Completado'),
  }
);
```

## Variables de entorno

| Variable                 | Por defecto                    |
|--------------------------|-------------------------------|
| `PORT`                   | `4000`                        |
| `MS_AUTH_URL`            | `http://localhost:8081`       |
| `MS_CLIENTES_URL`        | `http://localhost:8082`       |
| `MS_PEDIDOS_URL`         | `http://localhost:8083`       |
| `MS_FLOTA_URL`           | `http://localhost:8080`       |
| `MS_RUTEO_URL`           | `http://localhost:8085`       |
| `MS_SEGUIMIENTO_WS_URL`  | `http://localhost:3005`       |

## Ejecución local

```powershell
cd Backend/graphql-gateway
cp .env.example .env    # ajustar URLs si es necesario
npm install
npm start               # producción
npm run dev             # desarrollo con recarga automática
```

## Ejecución con Docker

```powershell
# Desde la raíz del proyecto
docker-compose up -d graphql-gateway
```

## Arquitectura interna

```
Cliente (browser / app)
        │
        ▼  HTTP POST /graphql  (queries + mutations)
        │  WS  ws://...        (subscriptions)
        │
┌───────────────────────┐
│   graphql-gateway     │
│                       │
│  Apollo Server 4      │
│  graphql-ws           │
│                       │
│  Resolvers            │
│  ├── Query            │───► ms-clientes  REST
│  │   ├── pedidosActivos     ms-pedidos   REST
│  │   ├── pedido             ms-flota     REST
│  │   ├── envio              ms-ruteo     REST
│  │   ├── clientes
│  │   └── vehiculosDisponibles
│  ├── Mutation         │───► ms-pedidos   REST
│  │   ├── crearPedido        ms-ruteo     REST
│  │   ├── cancelarPedido
│  │   └── actualizarEstadoEnvio
│  └── Subscription     │
│      └── posicionEnvio│
│                       │
│  SeguimientoManager   │◄──► ms-seguimiento  WebSocket
│  (Socket.IO client)   │
│                       │
│  Auth Middleware      │◄──► ms-auth  REST  /auth/verify
└───────────────────────┘
```
