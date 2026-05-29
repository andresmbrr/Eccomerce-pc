# Ecommerce-PC

Proyecto semestral DSY1103 desarrollado con arquitectura de microservicios usando Spring Boot, Spring Cloud, Eureka, API Gateway, OpenFeign, JPA/Hibernate, Spring Security, BCrypt y MySQL.

Cada microservicio posee su propia base de datos independiente para mantener desacoplamiento y autonomía.

Las entidades son mapeadas mediante JPA utilizando anotaciones como:

@Entity
@Table
@Id
@GeneratedValue
@Column
@ManyToOne

Hibernate se encarga de generar las tablas automáticamente mediante:

spring.jpa.hibernate.ddl-auto=update

Adicionalmente se incluyen scripts SQL de respaldo para facilitar la instalación y evaluación del proyecto.

Carpeta:

/sql
Scripts incluidos:
01-ms-auth.sql
02-ms-user.sql
03-ms-categorias.sql
04-ms-productos.sql
05-ms-stock.sql
06-ms-carrito.sql
07-ms-pedidos.sql
08-ms-pagos.sql
09-ms-notificaciones.sql
10-ms-reviews.sql

## Integrantes
- Andrés Bustamante
- Matías Latrach

## Arquitectura
El sistema usa microservicios independientes, cada uno con responsabilidad propia, `application.yml` propio y base de datos MySQL independiente.

## Tecnologías
- Java 21
- Spring Boot 3
- Spring Cloud
- Spring Security
- Spring Data JPA
- Hibernate ORM
- MySQL
- OpenFeign
- Eureka Server
- API Gateway
- Maven
- Lombok
- Bean Validation
- Postman

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
Se usa @Slf4j en controllers y services. ej: log.info("Pedido creado ID {}", saved.getId());
Se utilizan logs con @Slf4j para registrar:
- creación de recursos
- búsquedas
- actualizaciones
- errores
- llamadas Feign

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

2. Flujo funcional del ecommerce

Flujo de compra
1. Usuario se registra en ms-auth
2. Usuario crea perfil en ms-user
3. Usuario consulta productos
4. Usuario agrega productos al carrito
5. ms-carrito consulta stock mediante Feign
6. Usuario genera pedido
7. Usuario realiza pago
8. Se genera notificación
9. Usuario deja review


Te faltaría un diagrama simple tipo:

Cliente
   ↓
API Gateway
   ↓
Eureka Server
   ↓
Microservicios

y abajo:

ms-auth
ms-user
ms-productos
ms-stock
ms-carrito
ms-pedidos
ms-pagos
ms-notificaciones
ms-reviews
ms-categorias