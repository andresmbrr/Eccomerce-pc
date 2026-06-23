@echo off
title Detener Eccomerce - Modo Nativo
cls
echo ==========================================================
echo DETENIENDO ECCOMERCE - MODO NATIVO
echo ==========================================================
echo.
echo Se cerraran procesos Java asociados a los microservicios.
echo ADVERTENCIA: Si tienes otros programas Java abiertos, tambien podrían cerrarse.
echo.
pause
taskkill /F /IM java.exe
echo.
echo Procesos Java finalizados.
echo.
pause