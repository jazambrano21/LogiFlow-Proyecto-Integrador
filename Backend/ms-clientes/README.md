# ms-clientes

Microservicio REST de gestión de clientes y cuentas corporativas para LogiFlow Fase 2.

## Puerto

- REST API: `8082`
- Swagger/OpenAPI: `http://localhost:8082/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8082/api-docs`

## Endpoints

Todas las rutas requieren `Authorization: Bearer <token>` (obtenido de ms-auth).

| Método | Ruta                              | Descripción                        |
|--------|----------------------------------|------------------------------------|
| GET    | /clientes                        | Listar todos los clientes          |
| POST   | /clientes                        | Crear cliente                      |
| GET    | /clientes/{id}                   | Obtener cliente por ID             |
| PUT    | /clientes/{id}                   | Actualizar cliente                 |
| DELETE | /clientes/{id}                   | Eliminar cliente                   |
| POST   | /clientes/{id}/cuenta-corporativa | Crear cuenta corporativa          |
| GET    | /clientes/{id}/cuenta-corporativa | Obtener cuenta corporativa        |
| PUT    | /clientes/{id}/cuenta-corporativa | Actualizar cuenta corporativa     |

## Tipos

- `tipo` del cliente: `PERSONAL` o `CORPORATIVO`
- Solo clientes `CORPORATIVO` pueden tener cuenta corporativa

## Ejemplo crear cliente

```json
POST /clientes
Authorization: Bearer <token>
{
  "nombre": "Empresa ABC",
  "email": "abc@empresa.com",
  "telefono": "0991234567",
  "tipo": "CORPORATIVO"
}
```

## Variables de entorno

| Variable       | Por defecto                                           |
|---------------|-------------------------------------------------------|
| `PORT`        | `8082`                                                |
| `DB_URL`      | `jdbc:mysql://localhost:3311/ms_clientes_db?...`      |
| `DB_USERNAME` | `root`                                                |
| `DB_PASSWORD` | `root`                                                |
| `JWT_SECRET`  | `logiflow-secret-key-fase2-must-be-at-least-32-chars` |
| `MS_AUTH_URL` | `http://localhost:8081`                               |

## Ejecución local

```powershell
cd Backend/ms-clientes
docker-compose up -d        # MySQL en puerto 3311
.\mvnw.cmd spring-boot:run
```
