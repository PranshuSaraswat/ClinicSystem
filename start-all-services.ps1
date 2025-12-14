# Clinic Management System - Start All Services
# Run this script from the project root directory

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Clinic Management System - Startup" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Check if we're in the right directory
if (-not (Test-Path "eureka-server")) {
    Write-Host "Error: Please run this script from the ClinicSystem root directory" -ForegroundColor Red
    exit 1
}

# Function to check if port is in use
function Test-Port {
    param([int]$Port)
    $connection = Test-NetConnection -ComputerName localhost -Port $Port -WarningAction SilentlyContinue -InformationLevel Quiet
    return $connection
}

# Check if required ports are available
Write-Host "Checking required ports..." -ForegroundColor Yellow
$ports = @{8761="Eureka Server"; 8080="API Gateway"}
$portsInUse = @()

foreach ($port in $ports.Keys) {
    if (Test-Port -Port $port) {
        $portsInUse += "$port ($($ports[$port]))"
    }
}

if ($portsInUse.Count -gt 0) {
    Write-Host "Warning: The following ports are already in use:" -ForegroundColor Red
    $portsInUse | ForEach-Object { Write-Host "  - $_" -ForegroundColor Red }
    Write-Host ""
    $continue = Read-Host "Do you want to continue anyway? (y/n)"
    if ($continue -ne "y") {
        exit 0
    }
}

Write-Host ""
Write-Host "Starting all services..." -ForegroundColor Green
Write-Host "This will open 7 terminal windows" -ForegroundColor Yellow
Write-Host ""

# Start Eureka Server
Write-Host "[1/7] Starting Eureka Server on port 8761..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Write-Host 'Eureka Server' -ForegroundColor Cyan; cd '$PWD\eureka-server'; mvn spring-boot:run"
Write-Host "Waiting 15 seconds for Eureka to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 15

# Start Doctor Service
Write-Host "[2/7] Starting Doctor Service..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Write-Host 'Doctor Service' -ForegroundColor Cyan; cd '$PWD\doctor-service'; mvn spring-boot:run"
Start-Sleep -Seconds 8

# Start Patient Service
Write-Host "[3/7] Starting Patient Service..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Write-Host 'Patient Service' -ForegroundColor Cyan; cd '$PWD\patient-service'; mvn spring-boot:run"
Start-Sleep -Seconds 8

# Start Appointment Service
Write-Host "[4/7] Starting Appointment Service..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Write-Host 'Appointment Service' -ForegroundColor Cyan; cd '$PWD\appointment-service'; mvn spring-boot:run"
Start-Sleep -Seconds 8

# Start Billing Service
Write-Host "[5/7] Starting Billing Service..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Write-Host 'Billing Service' -ForegroundColor Cyan; cd '$PWD\billing-service'; mvn spring-boot:run"
Start-Sleep -Seconds 8

# Start Notification Service
Write-Host "[6/7] Starting Notification Service..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Write-Host 'Notification Service' -ForegroundColor Cyan; cd '$PWD\notification-service'; mvn spring-boot:run"
Start-Sleep -Seconds 8

# Start API Gateway
Write-Host "[7/7] Starting API Gateway on port 8080..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Write-Host 'API Gateway' -ForegroundColor Cyan; cd '$PWD\api-gateway'; mvn spring-boot:run"

Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "All services are starting!" -ForegroundColor Green
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Please wait 1-2 minutes for all services to fully start" -ForegroundColor Yellow
Write-Host ""
Write-Host "Important URLs:" -ForegroundColor Cyan
Write-Host "  Eureka Dashboard: http://localhost:8761" -ForegroundColor White
Write-Host "  API Gateway:      http://localhost:8080" -ForegroundColor White
Write-Host "  Frontend:         Open frontend/index.html in browser" -ForegroundColor White
Write-Host ""
Write-Host "To verify all services are running:" -ForegroundColor Cyan
Write-Host "  1. Open Eureka Dashboard (http://localhost:8761)" -ForegroundColor White
Write-Host "  2. Check that all 6 services are registered" -ForegroundColor White
Write-Host "  3. Open the frontend and test the system" -ForegroundColor White
Write-Host ""
Write-Host "To stop all services:" -ForegroundColor Cyan
Write-Host "  Press Ctrl+C in each terminal window" -ForegroundColor White
Write-Host ""
Write-Host "Press any key to open Eureka Dashboard..." -ForegroundColor Yellow
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

# Open Eureka Dashboard in default browser
Start-Process "http://localhost:8761"

Write-Host ""
Write-Host "Startup script completed!" -ForegroundColor Green
Write-Host "Check each terminal window for service status" -ForegroundColor Yellow
