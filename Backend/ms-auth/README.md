# ms-auth

Microservicio de autenticación y autorización JWT para LogiFlow Fase 2.

## Puerto

- REST API: `8081`
- Swagger/OpenAPI: `http://localhost:8081/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8081/api-docs`

## Endpoints

| Método | Ruta             | Auth    | Descripción                                         |
|--------|-----------------|---------|-----------------------------------------------------|
| POST   | /auth/register   | No      | Registra usuario con bcrypt. Roles: CLIENTE, CONDUCTOR, OPERADOR, ADMIN |
| POST   | /auth/login      | No      | Retorna JWT firmado `{ userId, email, rol }`        |
| GET    | /auth/verify     | Bearer  | Valida token y retorna payload decodificado         |

## Ejemplo register

```json
POST /auth/register
{
  "nombre": "Juan Pérez",
  "email": "juan@logiflow.com",
  "password": "segura123",
  "rol": "CLIENTE"
}
```

## Ejemplo login

```json
POST /auth/login
{ "email": "juan@logiflow.com", "password": "segura123" }

// Respuesta:
{ "success": true, "message": "Login exitoso", "data": { "token": "eyJhbGci..." } }
```

## Variables de entorno

| Variable         | Por defecto                                           |
|-----------------|-------------------------------------------------------|
| `PORT`          | `8081`                                                |
| `DB_URL`        | `jdbc:mysql://localhost:3310/ms_auth_db?...`          |
| `DB_USERNAME`   | `root`                                                |
| `DB_PASSWORD`   | `root`                                                |
| `JWT_SECRET`    | `logiflow-secret-key-fase2-must-be-at-least-32-chars` |
| `JWT_EXPIRATION`| `86400000` (24h en ms)                               |

## Ejecución local

```powershell
cd Backend/ms-auth
docker-compose up -d        # MySQL en puerto 3310
.\mvnw.cmd spring-boot:run
```
