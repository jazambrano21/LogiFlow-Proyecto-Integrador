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

---

# LogiFlow - Fase 2 (Etapa 1)

Tres nuevos microservicios REST independientes:

- `ms-auth` (puerto 8081): autenticación y emisión de JWT
- `ms-clientes` (puerto 8082): CRUD de clientes y cuentas corporativas
- `ms-pedidos` (puerto 8083): gestión de pedidos con eventos RabbitMQ

## Estructura Fase 2

```text
Backend/
├── ms-auth
├── ms-clientes
└── ms-pedidos
docker-compose.yml   ← levanta todo con un solo comando
.env.example         ← copia a .env y ajusta variables
```

## Requisitos

- Java 17
- Docker Desktop
- Maven Wrapper (`mvnw.cmd`)

## Levantar todo con Docker Compose

```powershell
# Desde la raíz del proyecto
cp .env.example .env   # solo la primera vez
docker-compose up -d --build
```

Servicios levantados:

| Servicio       | Puerto | Swagger                                  |
|---------------|--------|------------------------------------------|
| ms-auth       | 8081   | http://localhost:8081/swagger-ui.html    |
| ms-clientes   | 8082   | http://localhost:8082/swagger-ui.html    |
| ms-pedidos    | 8083   | http://localhost:8083/swagger-ui.html    |
| RabbitMQ UI   | 15672  | http://localhost:15672 (guest/guest)     |
| OpenAPI docs  | —      | /api-docs en cada servicio               |

## Ejecución local (sin Docker)

### ms-auth

```powershell
cd Backend/ms-auth
docker-compose up -d        # levanta mysql-auth en 3310
.\mvnw.cmd spring-boot:run
```

### ms-clientes

```powershell
cd Backend/ms-clientes
docker-compose up -d        # levanta mysql-clientes en 3311
.\mvnw.cmd spring-boot:run
```

### ms-pedidos

```powershell
cd Backend/ms-pedidos
docker-compose up -d        # levanta mysql-pedidos en 3312 y rabbitmq en 5672
.\mvnw.cmd spring-boot:run
```

## Endpoints ms-auth

| Método | Ruta           | Descripción                              | Auth |
|--------|---------------|------------------------------------------|------|
| POST   | /auth/register | Registrar usuario (bcrypt password)      | No   |
| POST   | /auth/login    | Login → retorna JWT                      | No   |
| GET    | /auth/verify   | Validar JWT → retorna payload decodificado | Bearer |

Roles disponibles: `CLIENTE`, `CONDUCTOR`, `OPERADOR`, `ADMIN`

## Endpoints ms-clientes

| Método | Ruta                              | Descripción                    |
|--------|----------------------------------|--------------------------------|
| GET    | /clientes                        | Listar clientes                |
| POST   | /clientes                        | Crear cliente                  |
| GET    | /clientes/{id}                   | Obtener cliente por ID         |
| PUT    | /clientes/{id}                   | Actualizar cliente             |
| DELETE | /clientes/{id}                   | Eliminar cliente               |
| POST   | /clientes/{id}/cuenta-corporativa | Crear cuenta corporativa       |
| GET    | /clientes/{id}/cuenta-corporativa | Obtener cuenta corporativa     |
| PUT    | /clientes/{id}/cuenta-corporativa | Actualizar cuenta corporativa  |

Todas las rutas requieren `Authorization: Bearer <token>`.

## Endpoints ms-pedidos

| Método | Ruta               | Descripción                                      |
|--------|--------------------|--------------------------------------------------|
| POST   | /pedidos           | Crear pedido → publica `pedido.creado` a RabbitMQ |
| GET    | /pedidos           | Listar (`?estado=CREADO&clienteId=1`)            |
| GET    | /pedidos/{id}      | Detalle del pedido                               |
| PUT    | /pedidos/{id}/estado | Actualizar estado (máquina de estados)          |
| DELETE | /pedidos/{id}      | Cancelar → publica `pedido.cancelado` a RabbitMQ |

Transiciones válidas de estado:
```
CREADO → ASIGNADO → EN_RUTA → ENTREGADO
CREADO → CANCELADO
ASIGNADO → CANCELADO
```

## RabbitMQ

- Exchange: `logistica.topic` (tipo topic, durable)
- `pedido.creado` → routing key `pedido.creado`
- `pedido.cancelado` → routing key `pedido.cancelado`

Payload del evento:
```json
{
  "pedidoId": 1,
  "clienteId": 5,
  "origen": { "ciudad": "Quito", "direccion": "Av. 6 de Diciembre" },
  "destino": { "ciudad": "Guayaquil", "direccion": "Av. 9 de Octubre" },
  "paquete": { "pesoKg": 2.5, "descripcion": "Documentos" },
  "nivelGeografico": "NACIONAL",
  "prioridad": "URGENTE",
  "timestamp": "2026-06-21T10:00:00"
}
```

## Puertos asignados (Fase 1 + Fase 2)

| Servicio     | App  | MySQL |
|-------------|------|-------|
| ms-flota    | 8080 | 3307  |
| ms-taller   | 8084 | 3309  |
| ms-auth     | 8081 | 3310  |
| ms-clientes | 8082 | 3311  |
| ms-pedidos  | 8083 | 3312  |

---

# LogiFlow - Fase 2 (Etapa 2)

Dos nuevos microservicios añadidos al ecosistema:

- `ms-ruteo` (puerto 8085): asignación automática/manual de envíos + consumidor de `pedido.creado`
- `ms-seguimiento` (puerto 3005 WebSocket): tracking en tiempo real, sin REST, solo Socket.IO + RabbitMQ

## Estructura completa Backend

```text
Backend/
├── ms-auth          → 8081  JWT auth
├── ms-clientes      → 8082  CRUD clientes
├── ms-pedidos       → 8083  pedidos + RabbitMQ publisher
├── ms-flota         → 8080  vehículos y conductores (Fase 1)
├── ms-taller        → 8084  mantenimiento (Fase 1)
├── ms-ruteo         → 8085  ruteo + asignación
└── ms-seguimiento   → 3005  WebSocket tracking
rabbitmq/
├── definitions.json  ← exchanges, colas y bindings pre-configurados
└── rabbitmq.conf     ← carga automática de definiciones al arrancar
```

## Levantar todo (Fase 2 completa)

```powershell
# Desde la raíz del proyecto
docker-compose up -d --build
```

## Puertos completos del sistema

| Servicio        | Puerto App | Puerto MySQL | Descripción                   |
|----------------|-----------|--------------|-------------------------------|
| ms-flota        | 8080      | 3307         | Fase 1 — vehículos            |
| ms-auth         | 8081      | 3310         | Fase 2 — autenticación JWT    |
| ms-clientes     | 8082      | 3311         | Fase 2 — clientes             |
| ms-pedidos      | 8083      | 3312         | Fase 2 — pedidos + eventos    |
| ms-taller       | 8084      | 3309         | Fase 1 — mantenimiento        |
| ms-ruteo        | 8085      | 3313         | Fase 2 — ruteo y envíos       |
| ms-seguimiento  | 3005 (WS) | —            | Fase 2 — tracking WebSocket   |
| RabbitMQ AMQP   | 5672      | —            | Mensajería                    |
| RabbitMQ UI     | 15672     | —            | http://localhost:15672        |

Credenciales RabbitMQ: `logiflow` / `logiflow2026`

## Arquitectura de eventos (Exchange: logistica.topic)

```
ms-pedidos  ──pedido.creado──►  q.ruteo.pedido-creado    → ms-ruteo (asigna)
                             ►  q.notificaciones.eventos  → (futuro ms-notificaciones)

ms-ruteo    ──envio.asignado──► (consumidores futuros)
            ──pedido.entregado► q.facturacion.pedido-entregado → (futuro ms-facturacion)

conductor   ──posicion.actualizada► q.seguimiento.posicion → ms-seguimiento → WebSocket
```

## Probar ms-seguimiento localmente

```powershell
cd Backend/ms-seguimiento
npm install
cp .env.example .env

# Terminal 1 — servidor WebSocket
npm start

# Terminal 2 — simulador de posiciones GPS (publica a RabbitMQ cada 3s)
npm run simulate -- --envioId=1
```

Cliente WebSocket de prueba (browser console o Node):

```js
const socket = io('http://localhost:3005');
socket.emit('subscribe', { envioId: 1 });
socket.on('posicion', (data) => console.log(data));
// { envioId: 1, lat: -0.18, lng: -78.46, velocidad: 72, eta: '...', timestamp: '...' }
```
