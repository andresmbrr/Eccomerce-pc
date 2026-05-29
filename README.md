# Ecommerce-PC

Proyecto semestral DSY1103 desarrollado con arquitectura de microservicios usando Spring Boot, Spring Cloud, Eureka, API Gateway, OpenFeign, JPA/Hibernate, Spring Security, BCrypt y MySQL.

## Integrantes
- Andrés Bustamante
- Matías Latrach

## Arquitectura
El sistema usa microservicios independientes, cada uno con responsabilidad propia, `application.yml` propio y base de datos MySQL independiente.

## Tecnologías
Java 21, Spring Boot, Spring Cloud, Eureka Server, API Gateway, OpenFeign, Spring Security, BCrypt, JPA/Hibernate, MySQL, Maven, Lombok, Bean Validation y Postman.

## Microservicios

| Servicio | Puerto | Base de datos | Responsabilidad |
|---|---:|---|---|
| eureka-server | 8761 | No aplica | Registro de servicios |
| api-gateway | 8080 | No aplica | Entrada centralizada |
| ms-auth | 8081 | ms_auth_db | Registro, login, roles y seguridad |
| ms-user | 8082 | ms_user_db | Perfil de usuario |
| ms-productos | 8083 | ms_productos_db | Productos |
| ms-stock | 8084 | ms_stock_db | Inventario |
| ms-carrito | 8085 | ms_carrito_db | Carrito |
| ms-pedidos | 8086 | ms_pedidos_db | Pedidos |
| ms-pagos | 8087 | ms_pagos_db | Pagos |
| ms-notificaciones | 8088 | ms_notificaciones_db | Notificaciones |
| ms-reviews | 8089 | ms_reviews_db | Reseñas |
| ms-categorias | 8090 | ms_categorias_db | Categorías |

## Bases de datos
ms_auth_db, ms_user_db, ms_categorias_db, ms_productos_db, ms_stock_db, ms_carrito_db, ms_pedidos_db, ms_pagos_db, ms_notificaciones_db y ms_reviews_db.

## Orden de ejecución
1. MySQL
2. eureka-server
3. api-gateway
4. ms-auth
5. ms-user
6. ms-categorias
7. ms-productos
8. ms-stock
9. ms-carrito
10. ms-pedidos
11. ms-pagos
12. ms-notificaciones
13. ms-reviews

## Eureka
http://localhost:8761

## Gateway
http://localhost:8080

## Rutas Gateway
/api/auth/**, /api/users/**, /api/categorias/**, /api/productos/**, /api/stock/**, /api/carrito/**, /api/pedidos/**, /api/pagos/**, /api/notificaciones/** y /api/reviews/**.

## Feign
Comunicación implementada:
- ms-carrito → ms-productos
- ms-carrito → ms-stock
- ms-pagos → ms-pedidos

## Validaciones
Se usa Bean Validation: @NotNull, @NotBlank, @Positive, @PositiveOrZero, @Min, @Max, @Size, @Email y @Past.

## Manejo de errores
Cada microservicio posee GlobalExceptionHandler para controlar 400, 404, 409 y 500.

## Logs
Se usa @Slf4j en controllers y services.

## Scripts SQL
Los scripts están en la carpeta /sql.

## Postman
Colección:
postman/Ecommerce-PC-EV2-Actualizado.postman_collection.json

Variable:
gateway = http://localhost:8080

## Orden recomendado de pruebas
1. Crear rol ADMIN
2. Registrar usuario
3. Crear perfil
4. Crear categoría
5. Crear producto
6. Crear stock
7. Agregar al carrito
8. Crear pedido
9. Crear pago
10. Crear notificación
11. Crear review

## Commit final recomendado
```bash
git add .
git commit -m "docs: update readme and postman collection for evaluation 2"
git push origin main
```
