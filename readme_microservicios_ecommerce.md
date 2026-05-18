# Ecommerce-PC — Arquitectura de Microservicios

Proyecto Ecommerce desarrollado con Spring Boot y Spring Cloud utilizando arquitectura de microservicios.

---

# Integrantes

- Andres Bustamante
- Matias Latrach

---

# Tecnologías Utilizadas

- Java 21
- Spring Boot 3.5.13
- Spring Cloud
- Eureka Server
- API Gateway
- OpenFeign
- Spring Security
- Spring Data JPA
- MySQL
- Maven
- Lombok
- Postman

---

# Arquitectura del Proyecto

El sistema está compuesto por los siguientes microservicios:

| Microservicio | Puerto | Responsabilidad |
|---|---|---|
| eureka-server | 8761 | Registro y descubrimiento |
| api-gateway | 8080 | Punto de entrada del sistema |
| ms-auth | 8081 | Registro, login y roles |
| ms-user | 8082 | Gestión de usuarios |
| ms-productos | 8083 | Gestión de productos |
| ms-stock | 8084 | Inventario y stock |
| ms-carrito | 8085 | Carrito de compras |
| ms-pedidos | 8086 | Gestión de pedidos |
| ms-pagos | 8087 | Procesamiento de pagos |
| ms-notificaciones | 8088 | Notificaciones |
| ms-reviews | 8089 | Comentarios y reseñas |

---

# Componentes Spring Cloud

## Eureka Server
Permite registrar y descubrir microservicios automáticamente.

## API Gateway
Centraliza el acceso a todos los microservicios mediante rutas unificadas.

## OpenFeign
Permite la comunicación entre microservicios.

Ejemplos implementados:

- ms-carrito → ms-stock
- ms-pedidos → ms-user
- ms-pedidos → ms-stock
- ms-pedidos → ms-pagos

---

# Seguridad

El microservicio `ms-auth` implementa:

- Registro de usuarios
- Login
- Roles
- BCrypt Password Encoder
- Spring Security

---

# Requisitos Previos

Antes de ejecutar el proyecto debes tener instalado:

- Java 21
- Maven
- MySQL
- Git
- VS Code o IntelliJ IDEA
- Postman

---

# Instalación del Proyecto

## 1. Clonar repositorio

```bash
git clone https://github.com/TU-USUARIO/Ecommerce-PC.git
```

---

## 2. Abrir proyecto

Abrir la carpeta del proyecto en:

- VS Code
- IntelliJ IDEA

---

## 3. Base de Datos

Las bases de datos y tablas se crean automáticamente al ejecutar los microservicios por primera vez.

Solo es necesario configurar correctamente las credenciales de MySQL en cada microservicio:

```properties
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
```

---

## 4. Configurar credenciales

Modificar en cada microservicio:

```properties
application.properties
```

Las credenciales de MySQL:

```properties
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
```

---

## 5. Instalar dependencias Maven

Desde terminal ejecutar:

```bash
mvn clean install
```

---

# Orden de Ejecución

Ejecutar los microservicios en este orden:

1. eureka-server
2. api-gateway
3. ms-auth
4. ms-user
5. ms-productos
6. ms-stock
7. ms-carrito
8. ms-pedidos
9. ms-pagos
10. ms-notificaciones
11. ms-reviews

---

# Acceso Eureka

```txt
http://localhost:8761
```

---

# Acceso API Gateway

```txt
http://localhost:8080
```

---

# Endpoints Principales

## Productos

```http
GET /api/productos
POST /api/productos
```

## Stock

```http
GET /api/stock
POST /api/stock
```

## Pedidos

```http
GET /api/pedidos
POST /api/pedidos
```

## Auth

```http
POST /api/auth/register
POST /api/auth/register/login
POST /api/auth/roles
```

---

# Pruebas

Las pruebas fueron realizadas utilizando Postman a través del API Gateway.

---

# Objetivo del Proyecto

Implementar una arquitectura de microservicios desacoplada utilizando Spring Boot y Spring Cloud aplicando:

- Service Discovery
- API Gateway
- Comunicación entre microservicios
- Seguridad
- Arquitectura distribuida
- Buenas prácticas backend

---

# Estado del Proyecto

Proyecto funcional y operativo.

