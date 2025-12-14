@echo off
REM Clinic Management System - Start All Services
REM Run this script from the project root directory

echo =====================================
echo Clinic Management System - Startup
echo =====================================
echo.

REM Check if we're in the right directory
if not exist "eureka-server" (
    echo Error: Please run this script from the ClinicSystem root directory
    pause
    exit /b 1
)

echo Starting all services...
echo This will open 7 command prompt windows
echo.

REM Start Eureka Server
echo [1/7] Starting Eureka Server on port 8761...
start "Eureka Server" cmd /k "cd eureka-server && mvn spring-boot:run"
timeout /t 15 /nobreak

REM Start Doctor Service
echo [2/7] Starting Doctor Service...
start "Doctor Service" cmd /k "cd doctor-service && mvn spring-boot:run"
timeout /t 8 /nobreak

REM Start Patient Service
echo [3/7] Starting Patient Service...
start "Patient Service" cmd /k "cd patient-service && mvn spring-boot:run"
timeout /t 8 /nobreak

REM Start Appointment Service
echo [4/7] Starting Appointment Service...
start "Appointment Service" cmd /k "cd appointment-service && mvn spring-boot:run"
timeout /t 8 /nobreak

REM Start Billing Service
echo [5/7] Starting Billing Service...
start "Billing Service" cmd /k "cd billing-service && mvn spring-boot:run"
timeout /t 8 /nobreak

REM Start Notification Service
echo [6/7] Starting Notification Service...
start "Notification Service" cmd /k "cd notification-service && mvn spring-boot:run"
timeout /t 8 /nobreak

REM Start API Gateway
echo [7/7] Starting API Gateway on port 8080...
start "API Gateway" cmd /k "cd api-gateway && mvn spring-boot:run"

echo.
echo =====================================
echo All services are starting!
echo =====================================
echo.
echo Please wait 1-2 minutes for all services to fully start
echo.
echo Important URLs:
echo   Eureka Dashboard: http://localhost:8761
echo   API Gateway:      http://localhost:8080
echo   Frontend:         Open frontend/index.html in browser
echo.
echo To stop all services:
echo   Close each command prompt window or press Ctrl+C
echo.
echo Press any key to open Eureka Dashboard...
pause >nul

REM Open Eureka Dashboard
start http://localhost:8761

echo.
echo Startup script completed!
echo Check each window for service status
pause
