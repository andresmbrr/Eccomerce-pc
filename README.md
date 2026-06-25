# Ecommerce-PC

# 🚀 SISTEMA DE MICROSERVICIOS MULTIMÓDULO - ENTREGA FINAL

## 📦 Componentes de Distribución y Defensa Técnica

| Componente | Descripción | Enlace |
|------------|------------|---------|
| **📦 Versión Sin Docker (Arranque Nativo)** | Archivo `.zip` que contiene la carpeta `apps/` con todos los `.jar` compilados y el script `arrancar-nativo.bat`. | https://drive.google.com/drive/folders/1SxwfUTDvj1bqjNIYHsQqSrQk1GeoS1Di |
| **🐳 Versión Docker (Avance Examen Transversal)** | Versión preparada para contenerización mediante Docker Compose. No forma parte de la entrega oficial de esta evaluación. | N/A |
| **🎥 Video de Defensa Técnica (Evaluación Individual)** | Video explicativo donde se presenta la arquitectura, ejecución, pruebas unitarias, documentación Swagger/OpenAPI y aporte técnico individual. | Próximamente |
> La entrega oficial corresponde a la versión nativa (sin Docker).

---

# 📖 Descripción General

Ecommerce-PC es una plataforma Ecommerce desarrollada con arquitectura de microservicios utilizando Spring Boot y Spring Cloud.

El sistema permite administrar autenticación, usuarios, categorías, productos, inventario, carrito de compras, pedidos, pagos, notificaciones y reseñas, utilizando una base de datos independiente por microservicio.

---

# 👥 Integrantes

- Andrés Bustamante
- Matías Latrach

---

# 🎯 Objetivo

Implementar una solución distribuida basada en microservicios aplicando:

- Spring Boot 3
- Spring Cloud
- Eureka Server
- API Gateway
- OpenFeign
- Spring Security
- Swagger/OpenAPI
- Testing con JUnit y Mockito
- Persistencia MySQL

---

# 🏗 Arquitectura General

```text
Cliente / Postman
        |
        v
   API Gateway :8080
        |
        v
  Eureka Server :8761
        |
        +--> ms-auth           :8081
        +--> ms-user           :8082
        +--> ms-productos      :8083
        +--> ms-stock          :8084
        +--> ms-carrito        :8085
        +--> ms-pedidos        :8086
        +--> ms-pagos          :8087
        +--> ms-notificaciones :8088
        +--> ms-reviews        :8089
        +--> ms-categorias     :8090
```

---

# 📦 Proyecto Maven Multimódulo

El sistema utiliza una estructura Maven Padre-Hijos.

```text
Ecommerce-PC-parent/
│
├── pom.xml
├── eureka-server/
├── api-gateway/
├── ms-auth/
├── ms-user/
├── ms-categorias/
├── ms-productos/
├── ms-stock/
├── ms-carrito/
├── ms-pedidos/
├── ms-pagos/
├── ms-notificaciones/
└── ms-reviews/
```

---

# 🛠 Tecnologías Utilizadas

- Java 21
- Spring Boot 3
- Spring Cloud
- Spring Security
- BCrypt
- Spring Data JPA
- Hibernate ORM
- MySQL
- OpenFeign
- Eureka Server
- API Gateway
- Lombok
- Bean Validation
- Swagger / OpenAPI
- Maven
- JUnit 5
- Mockito
- MockMvc
- Postman

---

# 🔧 Microservicios

| Servicio | Puerto | Responsabilidad |
|-----------|---------:|----------------|
| eureka-server | 8761 | Descubrimiento de servicios |
| api-gateway | 8080 | Punto único de entrada |
| ms-auth | 8081 | Usuarios, roles y autenticación |
| ms-user | 8082 | Perfiles |
| ms-productos | 8083 | Productos |
| ms-stock | 8084 | Inventario |
| ms-carrito | 8085 | Carrito |
| ms-pedidos | 8086 | Pedidos |
| ms-pagos | 8087 | Pagos |
| ms-notificaciones | 8088 | Notificaciones |
| ms-reviews | 8089 | Reseñas |
| ms-categorias | 8090 | Categorías |

---

# 🗄 Bases de Datos

| Microservicio | Base de Datos |
|---------------|---------------|
| ms_auth_db | Autenticación |
| ms_user_db | Usuarios |
| ms_categorias_db | Categorías |
| ms_productos_db | Productos |
| ms_stock_db | Stock |
| ms_carrito_db | Carrito |
| ms_pedidos_db | Pedidos |
| ms_pagos_db | Pagos |
| ms_notificaciones_db | Notificaciones |
| ms_reviews_db | Reviews |

Configuración ORM:

```properties
spring.jpa.hibernate.ddl-auto=update
```

---

# 🚀 Compilación

```bash
./mvnw clean install
```

```bash
./mvnw clean install -DskipTests
```

```bash
mvn clean install
```

```bash
mvn clean install -DskipTests
```

---
# ▶ Ejecución Nativa del Sistema

La entrega incluye el script:

```bat
arrancar-nativo.bat
```

Este script automatiza el inicio de todos los componentes del sistema respetando el orden requerido por la arquitectura distribuida.

## Secuencia de Arranque

1. MySQL
2. Eureka Server
3. Microservicios
4. API Gateway

### Orden de Inicio de Microservicios

1. ms-auth
2. ms-user
3. ms-categorias
4. ms-productos
5. ms-stock
6. ms-carrito
7. ms-pedidos
8. ms-pagos
9. ms-notificaciones
10. ms-reviews

Una vez iniciados todos los servicios, el API Gateway queda disponible en:

```text
http://localhost:8080
```

# ▶ Orden de Ejecución

1. MySQL
2. Eureka Server
3. API Gateway
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

---

# 🌐 Eureka

```text
http://localhost:8761
```

---

# 🚪 API Gateway

```text
http://localhost:8080
```

Rutas:

```text
/api/auth/**
/api/users/**
/api/categorias/**
/api/productos/**
/api/stock/**
/api/carrito/**
/api/pedidos/**
/api/pagos/**
/api/notificaciones/**
/api/reviews/**
```

---

# 📖 Swagger / OpenAPI

| Servicio | URL |
|-----------|-----|
| ms-auth | http://localhost:8081/doc/swagger-ui.html |
| ms-user | http://localhost:8082/doc/swagger-ui.html |
| ms-productos | http://localhost:8083/doc/swagger-ui.html |
| ms-stock | http://localhost:8084/doc/swagger-ui.html |
| ms-carrito | http://localhost:8085/doc/swagger-ui.html |
| ms-pedidos | http://localhost:8086/doc/swagger-ui.html |
| ms-pagos | http://localhost:8087/doc/swagger-ui.html |
| ms-notificaciones | http://localhost:8088/doc/swagger-ui.html |
| ms-reviews | http://localhost:8089/doc/swagger-ui.html |
| ms-categorias | http://localhost:8090/doc/swagger-ui.html |

---

# 🔗 Comunicación entre Microservicios

OpenFeign:

- ms-carrito → ms-productos
- ms-carrito → ms-stock
- ms-pagos → ms-pedidos

---

# 🔐 Seguridad

Implementación mediante:

- Spring Security
- BCrypt Password Encoder
- Roles de usuario
- Login seguro
- Protección de contraseñas

---

# ✅ Validaciones

Uso de Bean Validation:

```java
@NotNull
@NotBlank
@Email
@Size
@Min
@Max
@Positive
@PositiveOrZero
```

---

# ⚠ Manejo de Excepciones

Todos los microservicios implementan:

```java
@RestControllerAdvice
```

Controlando errores HTTP:

- 400
- 404
- 409
- 500

---

# 📝 Logging

Implementado mediante:

```java
@Slf4j
```

---

# 🧪 Pruebas Unitarias

Tecnologías utilizadas:

- JUnit 5
- Mockito
- MockMvc

Cobertura:

| Microservicio | Controller | Service |
|---------------|-----------|----------|
| ms-auth | ✅ | ✅ |
| ms-user | ✅ | ✅ |
| ms-categorias | ✅ | ✅ |
| ms-productos | ✅ | ✅ |
| ms-stock | ✅ | ✅ |
| ms-carrito | ✅ | ✅ |
| ms-pedidos | ✅ | ✅ |
| ms-pagos | ✅ | ✅ |
| ms-notificaciones | ✅ | ✅ |
| ms-reviews | ✅ | ✅ |

---
# 🧪 Ejecución de Pruebas Unitarias

La suite completa de pruebas unitarias puede ejecutarse desde la raíz del proyecto mediante Maven.

## Ejecutar todas las pruebas

```bash
mvn clean install
```

o utilizando Maven Wrapper:

```bash
./mvnw clean install
```

Durante el proceso de compilación se ejecutan automáticamente todas las pruebas unitarias desarrolladas con:

- JUnit 5
- Mockito
- MockMvc

Las pruebas cubren:

- Controladores REST.
- Servicios.
- Validaciones.
- Casos exitosos.
- Casos de error.
- Manejo de excepciones.

# 📬 Postman

Colección:

```text
postman/Ecommerce-PC-EV2-Actualizado.postman_collection.json
```

Variable:

```text
gateway=http://localhost:8080
```

---

# 🗃 Scripts SQL

```text
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
```

---

# 🛒 Flujo Funcional

1. Registro de usuario.
2. Creación de perfil.
3. Creación de categorías.
4. Registro de productos.
5. Gestión de stock.
6. Agregar al carrito.
7. Generar pedido.
8. Registrar pago.
9. Generar notificación.
10. Registrar reseña.

---

# 📈 Estado Actual

| Componente | Estado |
|------------|---------|
| Maven Multimódulo | ✅ |
| Eureka Server | ✅ |
| API Gateway | ✅ |
| OpenFeign | ✅ |
| Swagger/OpenAPI | ✅ |
| Spring Security | ✅ |
| BCrypt | ✅ |
| MySQL | ✅ |
| JPA/Hibernate | ✅ |
| Bean Validation | ✅ |
| Logging | ✅ |
| Testing Controller | ✅ |
| Testing Service | ✅ |
| Postman | ✅ |
| Scripts SQL | ✅ |

---

# 📌 Conclusión

Ecommerce-PC demuestra la implementación de una arquitectura de microservicios moderna utilizando tecnologías del ecosistema Spring, incorporando seguridad, documentación, pruebas unitarias, comunicación distribuida y persistencia desacoplada.

# 🎥 Video de Defensa Técnica

La evaluación individual considera un video explicativo donde se demuestra el funcionamiento completo del sistema.

## Contenido del Video

- Arquitectura de microservicios.
- Registro en Eureka Server.
- API Gateway.
- Comunicación mediante OpenFeign.
- Documentación Swagger/OpenAPI.
- Ejecución de pruebas unitarias.
- Flujo funcional del Ecommerce.
- Aporte técnico individual.

## Requisitos

- Duración ideal: 15 minutos.
- Duración máxima permitida: 18 minutos.
- Audio claro y comprensible.
- Explicación técnica individual.

## Archivos Complementarios

La entrega incluye:

```text
subtitulos-video.txt
```

Este archivo contiene la transcripción y subtítulos utilizados durante la grabación de la defensa técnica.
