@echo off
title Eccomerce - Ejecucion Nativa de Microservicios
cls
echo ==========================================================
echo INICIANDO ECCOMERCE - MODO NATIVO SIN DOCKER
echo ==========================================================
echo.
echo Verificando Java instalado...
java -version
echo.
echo IMPORTANTE:
echo - MySQL debe estar iniciado.
echo - Las bases de datos deben existir.
echo - Los puertos 8761, 8080, 8081, 8082, 8083, 8084, 8085, 8086, 8087, 8088, 8089 Y 8090 deben estar disponibles.
echo.
echo ==========================================================
echo [1/3] Iniciando Eureka Server...
echo ==========================================================
start "Eureka Server" cmd /k "java -jar apps/eureka-server.jar"
echo Esperando 20 segundos para que Eureka inicie...
timeout /t 20 /nobreak > nul
echo.
echo ==========================================================
echo [2/3] Iniciando microservicios de negocio...
echo ==========================================================
echo Iniciando ms-auth...
start "MS Auth" cmd /k "java -jar apps/ms-auth.jar"
echo Iniciando ms-carrito...
start "MS Carrito" cmd /k "java -jar apps/ms-carrito.jar"
echo Iniciando ms-categorias...
start "MS Categorias" cmd /k "java -jar apps/ms-categorias.jar"
echo Iniciando ms-notificaciones...
start "MS Notificaciones" cmd /k "java -jar apps/ms-notificaciones.jar"
echo Iniciando ms-pagos...
start "MS Pagos" cmd /k "java -jar apps/ms-pagos.jar"
echo Iniciando ms-pedidos...
start "MS Pedidos" cmd /k "java -jar apps/ms-pedidos.jar"
echo Iniciando ms-productos...
start "MS Productos" cmd /k "java -jar apps/ms-productos.jar"
echo Iniciando ms-reviews...
start "MS Reviews" cmd /k "java -jar apps/ms-reviews.jar"
echo Iniciando ms-stock...
start "MS Stock" cmd /k "java -jar apps/ms-stock.jar"
echo Iniciando ms-user...
start "MS User" cmd /k "java -jar apps/ms-user.jar"
echo Esperando 25 segundos para que los microservicios se registren en Eureka...
timeout /t 25 /nobreak > nul
echo.
echo ==========================================================
echo [3/3] Iniciando API Gateway...
echo ==========================================================
start "API Gateway" cmd /k "java -jar apps/api-gateway.jar"
echo.
echo ==========================================================
echo ECCOMERCE INICIADO EN MODO NATIVO
echo ==========================================================
echo.
echo Verificar Eureka:
echo http://localhost:8761
echo.
echo Verificar API Gateway:
echo http://localhost:8080
echo.
echo Cada microservicio se ejecuto en una ventana separada.
echo No cerrar las ventanas de los servicios mientras se este probando.
echo.
pause