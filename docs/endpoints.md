# Documentación de Endpoints - Ecommerce-PC

Base URL por API Gateway:

```txt
http://localhost:8080

| Método | Endpoint               | Descripción                |
| ------ | ---------------------- | -------------------------- |
| POST   | `/api/auth/roles`      | Crear rol                  |
| GET    | `/api/auth/roles`      | Listar roles               |
| POST   | `/api/auth/register`   | Registrar usuario          |
| POST   | `/api/auth/login`      | Iniciar sesión             |
| GET    | `/api/auth/users`      | Listar usuarios auth       |
| GET    | `/api/auth/users/{id}` | Buscar usuario auth por ID |
| PUT    | `/api/auth/users/{id}` | Actualizar usuario auth    |
| DELETE | `/api/auth/users/{id}` | Eliminar usuario auth      |

| Método | Endpoint          | Descripción             |
| ------ | ----------------- | ----------------------- |
| POST   | `/api/users`      | Crear perfil de usuario |
| GET    | `/api/users`      | Listar perfiles         |
| GET    | `/api/users/{id}` | Buscar perfil por ID    |
| PUT    | `/api/users/{id}` | Actualizar perfil       |
| DELETE | `/api/users/{id}` | Eliminar perfil         |
