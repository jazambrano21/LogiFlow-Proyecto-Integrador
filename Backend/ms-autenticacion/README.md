# ms-autenticacion

Microservicio de Autenticación y Gestión de Usuarios para el sistema LogiFlow.

Este servicio centraliza la autenticación mediante JWT, el registro de usuarios y la administración de roles dentro de la plataforma LogiFlow.

## Variables de Entorno

| Variable         | Descripción                              | Valor por defecto          |
|------------------|------------------------------------------|----------------------------|
| `DB_HOST`        | Host de la base de datos PostgreSQL      | `localhost`                |
| `DB_PORT`        | Puerto de PostgreSQL                     | `5432`                     |
| `DB_NAME`        | Nombre de la base de datos               | `db_auth`                  |
| `DB_USER`        | Usuario de la base de datos              | `logiflow`                 |
| `DB_PASS`        | Contraseña de la base de datos           | `logiflow123`              |
| `JWT_SECRET`     | Clave secreta para firmar tokens JWT     | `logiflow-secret-key-2026` |
| `JWT_EXPIRATION_MS` | Tiempo de expiración del JWT en ms    | `86400000` (24h)           |

## Instrucciones de ejecución

1. Crear la red externa compartida (una sola vez):
   ```bash
   docker network create logiflow-network
   ```

2. Levantar el servicio con Docker Compose:
   ```bash
   docker-compose up --build
   ```

3. La documentación Swagger estará disponible en:
   ```
   http://localhost:8081/swagger-ui.html
   ```

## Endpoints

| Método | Ruta                            | Autenticación | Descripción                              |
|--------|---------------------------------|---------------|------------------------------------------|
| POST   | `/api/v1/auth/register`         | Público       | Registrar un nuevo usuario               |
| POST   | `/api/v1/auth/login`              | Público       | Iniciar sesión y obtener JWT             |
| GET    | `/api/v1/auth/verify`             | Público       | Verificar validez del token JWT          |
| GET    | `/api/v1/auth/usuarios`           | ADMIN         | Listar usuarios paginados                |
| PUT    | `/api/v1/auth/usuarios/{id}/rol`  | ADMIN         | Cambiar el rol de un usuario             |

## Ejemplos curl

### Registro de usuario
```bash
curl -X POST http://localhost:8081/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan",
    "apellido": "Perez",
    "email": "juan@logiflow.com",
    "password": "password123",
    "rol": "CLIENTE"
  }'
```

### Login
```bash
curl -X POST http://localhost:8081/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@logiflow.com",
    "password": "admin123"
  }'
```

### Verificar token
```bash
curl -X GET http://localhost:8081/api/v1/auth/verify \
  -H "Authorization: Bearer <TU_JWT_AQUI>"
```

## Seed de datos

Al iniciar, si la tabla de usuarios está vacía, se crean automáticamente:

| Email                | Contraseña   | Rol      |
|----------------------|--------------|----------|
| `admin@logiflow.com` | `admin123`   | ADMIN    |
| `operador@logiflow.com` | `operador123` | OPERADOR |

## Stack Tecnológico

- Java 17
- Spring Boot 3.2
- Spring Security
- JWT (jjwt 0.11.5)
- Spring Data JPA
- PostgreSQL 15
- SpringDoc OpenAPI 3
- Maven
- Docker
