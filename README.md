# Ecommerce-PC
# 🚀 SISTEMA DE MICROSERVICIOS MULTIMÓDULO - ENTREGA FINAL

## 📦 COMPONENTES DE DISTRIBUCIÓN Y DEFENSA TÉCNICA

Utilice los siguientes enlaces externos para descargar las versiones listas para producción y visualizar la defensa del proyecto:

| Componente | Descripción | Enlace de Descarga (Nube externa) |

| :--- | :--- | :--- |

| **📦 Versión Sin Docker** <br>*(Arranque Nativo)* | Archivo `.zip` que contiene la carpeta `apps/` con los `.jar` compilados y el script `arrancar-nativo.bat` ordenado por fases. | [Descargar ZIP Nativo aquí](https://drive.google.com/drive/folders/1SxwfUTDvj1bqjNIYHsQqSrQk1GeoS1Di?usp=drive_link) |

| **🐳 Versión Con Docker** <br>*(Avance Examen Transversal)* | Archivo `.zip` que contiene la carpeta `apps/` con los `.jar`, el archivo `docker-compose.yml` y el script automatizado `arrancar-sistema.bat`. | [Descargar ZIP Docker aquí](ENLACE_A_DRIVE_AQUÍ) |

| **🎥 Video de Defensa Técnica** <br>*(Evaluación Individual)* | Enlace directo al video explicativo donde se evidencia el funcionamiento, testing y el aporte técnico individual. **Duración ideal: 15 minutos (Máximo permitido: 18 minutos).** | [Ver Video Explicativo aquí](ENLACE_A_VIDEO_AQUÍ) |

Ecommerce-PC
🚀 Sistema Ecommerce basado en Arquitectura de Microservicios

Proyecto semestral desarrollado para la asignatura DSY1103, implementando una arquitectura distribuida basada en microservicios utilizando Spring Boot y Spring Cloud.

El sistema permite gestionar usuarios, autenticación, productos, categorías, inventario, carrito de compras, pedidos, pagos, notificaciones y reseñas, siguiendo principios de desacoplamiento, escalabilidad y responsabilidad única.

👥 Integrantes
Andrés Bustamante
Matías Latrach
🎯 Objetivo del Proyecto

Desarrollar una plataforma Ecommerce utilizando arquitectura de microservicios que permita:

Registro y autenticación de usuarios.
Administración de perfiles.
Gestión de categorías y productos.
Control de inventario.
Administración de carrito de compras.
Gestión de pedidos.
Registro de pagos.
Generación de notificaciones.
Gestión de reseñas de productos.
🏗 Arquitectura General
Cliente / Postman / Navegador
            |
            v
      API Gateway
        :8080
            |
            v
      Eureka Server
        :8761
            |
            +------------------+
            |                  |
            v                  v

ms-auth              :8081
ms-user              :8082
ms-productos         :8083
ms-stock             :8084
ms-carrito           :8085
ms-pedidos           :8086
ms-pagos             :8087
ms-notificaciones    :8088
ms-reviews           :8089
ms-categorias        :8090
📦 Proyecto Maven Multimódulo

El sistema fue desarrollado utilizando una estructura Maven Padre-Hijos.

El módulo raíz centraliza:

Dependencias compartidas.
Plugins Maven.
Configuración de compilación.
Gestión de versiones.
Construcción completa del sistema.

Cada microservicio posee su propio pom.xml heredando del proyecto padre.

📁 Estructura del Proyecto
Ecommerce-PC-parent/
│
├── pom.xml
├── README.md
│
├── eureka-server/
├── api-gateway/
│
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
│
├── sql/
│   ├── 01-ms-auth.sql
│   ├── 02-ms-user.sql
│   ├── 03-ms-categorias.sql
│   ├── 04-ms-productos.sql
│   ├── 05-ms-stock.sql
│   ├── 06-ms-carrito.sql
│   ├── 07-ms-pedidos.sql
│   ├── 08-ms-pagos.sql
│   ├── 09-ms-notificaciones.sql
│   └── 10-ms-reviews.sql
│
└── postman/
    └── Ecommerce-PC-EV2-Actualizado.postman_collection.json
🛠 Tecnologías Utilizadas
Java 21
Spring Boot 3
Spring Cloud
Spring Security
BCrypt
Spring Data JPA
Hibernate ORM
MySQL
OpenFeign
Eureka Server
API Gateway
Bean Validation
Lombok
Swagger / OpenAPI
JUnit 5
Mockito
MockMvc
Maven
Postman
🔧 Microservicios
Servicio	Puerto	Responsabilidad
Eureka Server	8761	Registro y descubrimiento
API Gateway	8080	Punto único de entrada
ms-auth	8081	Autenticación y roles
ms-user	8082	Perfiles de usuario
ms-productos	8083	Productos
ms-stock	8084	Inventario
ms-carrito	8085	Carrito de compras
ms-pedidos	8086	Gestión de pedidos
ms-pagos	8087	Gestión de pagos
ms-notificaciones	8088	Notificaciones
ms-reviews	8089	Reseñas
ms-categorias	8090	Categorías
🗄 Bases de Datos

Cada microservicio posee una base de datos independiente.

Microservicio	Base de Datos
ms-auth	ms_auth_db
ms-user	ms_user_db
ms-categorias	ms_categorias_db
ms-productos	ms_productos_db
ms-stock	ms_stock_db
ms-carrito	ms_carrito_db
ms-pedidos	ms_pedidos_db
ms-pagos	ms_pagos_db
ms-notificaciones	ms_notificaciones_db
ms-reviews	ms_reviews_db

Hibernate genera automáticamente las tablas mediante:

spring.jpa.hibernate.ddl-auto=update
🚀 Compilación del Proyecto

Desde la raíz del proyecto:

./mvnw clean install

Compilar omitiendo pruebas:

./mvnw clean install -DskipTests

Utilizando Maven instalado globalmente:

mvn clean install
mvn clean install -DskipTests
▶ Orden de Ejecución
MySQL
Eureka Server
API Gateway
ms-auth
ms-user
ms-categorias
ms-productos
ms-stock
ms-carrito
ms-pedidos
ms-pagos
ms-notificaciones
ms-reviews
🌐 Eureka Server
http://localhost:8761

Servicios registrados:

API-GATEWAY
MS-AUTH
MS-USER
MS-CATEGORIAS
MS-PRODUCTOS
MS-STOCK
MS-CARRITO
MS-PEDIDOS
MS-PAGOS
MS-NOTIFICACIONES
MS-REVIEWS
🚪 API Gateway
http://localhost:8080

Rutas principales:

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
📖 Swagger / OpenAPI

Cada microservicio expone documentación automática mediante SpringDoc OpenAPI.

Servicio	URL Swagger
ms-auth	http://localhost:8081/doc/swagger-ui.html
ms-user	http://localhost:8082/doc/swagger-ui.html
ms-productos	http://localhost:8083/doc/swagger-ui.html
ms-stock	http://localhost:8084/doc/swagger-ui.html
ms-carrito	http://localhost:8085/doc/swagger-ui.html
ms-pedidos	http://localhost:8086/doc/swagger-ui.html
ms-pagos	http://localhost:8087/doc/swagger-ui.html
ms-notificaciones	http://localhost:8088/doc/swagger-ui.html
ms-reviews	http://localhost:8089/doc/swagger-ui.html
ms-categorias	http://localhost:8090/doc/swagger-ui.html
🔗 Comunicación entre Microservicios (Feign)
Servicio Origen	Servicio Destino
ms-carrito	ms-productos
ms-carrito	ms-stock
ms-pagos	ms-pedidos
🔐 Seguridad

El sistema implementa:

Spring Security
BCrypt Password Encoder
Gestión de Roles
Registro de Usuarios
Login Seguro
Protección de Contraseñas Encriptadas
✅ Validaciones

Se implementan validaciones utilizando Bean Validation:

@NotNull
@NotBlank
@Positive
@PositiveOrZero
@Min
@Max
@Size
@Email
@Past
⚠ Manejo Global de Errores

Todos los microservicios incorporan:

@RestControllerAdvice

Manejando respuestas:

400 Bad Request
404 Not Found
409 Conflict
500 Internal Server Error
📝 Logs

Se implementan logs mediante:

@Slf4j

Registrando:

Creación de recursos
Actualizaciones
Eliminaciones
Consultas
Errores
Comunicación Feign

Ejemplo:

log.info("Pedido creado ID {}", saved.getId());
🧪 Pruebas Unitarias

Se implementaron pruebas unitarias utilizando:

JUnit 5
Mockito
MockMvc

Cobertura aplicada sobre:

Controllers
Services
Validaciones
Casos exitosos
Casos de error
📊 Cobertura de Testing
Microservicio	Controller Test	Service Test
ms-auth	✅	✅
ms-user	✅	✅
ms-categorias	✅	✅
ms-productos	✅	✅
ms-stock	✅	✅
ms-carrito	✅	✅
ms-pedidos	✅	✅
ms-pagos	✅	✅
ms-notificaciones	✅	✅
ms-reviews	✅	✅
🧪 Ejecución de Pruebas Unitarias

Ejemplo:

./mvnw -pl ms-productos -Dtest=ProductControllerTest test

./mvnw -pl ms-productos -Dtest=ProductServiceImplTest test

Para ejecutar todas las pruebas:

mvn test
📬 Postman

Colección incluida:

postman/Ecommerce-PC-EV2-Actualizado.postman_collection.json

Variable utilizada:

gateway=http://localhost:8080
🗃 Scripts SQL

Ubicación:

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
🛒 Flujo Funcional del Ecommerce
Usuario se registra en ms-auth.
Usuario crea perfil en ms-user.
Usuario consulta productos.
Usuario agrega productos al carrito.
ms-carrito valida stock mediante Feign.
Usuario genera pedido.
Usuario realiza pago.
Se genera notificación.
Usuario registra una reseña.
📈 Estado Actual del Proyecto
Componente	Estado
Maven Multimódulo	✅
Eureka Server	✅
API Gateway	✅
Spring Security	✅
BCrypt	✅
Swagger/OpenAPI	✅
OpenFeign	✅
MySQL	✅
JPA/Hibernate	✅
Bean Validation	✅
Logging	✅
Manejo de Excepciones	✅
Postman	✅
Scripts SQL	✅
Testing Controller	✅
Testing Service	✅
Docker	🚧 En desarrollo
📌 Conclusión

Ecommerce-PC implementa una arquitectura moderna basada en microservicios utilizando el ecosistema Spring, aplicando buenas prácticas de desacoplamiento, documentación, seguridad, pruebas unitarias y comunicación distribuida entre servicios.

El proyecto demuestra la integración de tecnologías empresariales ampliamente utilizadas en entornos reales de desarrollo backend.
ms-pagos
ms-notificaciones
ms-reviews
ms-categorias
