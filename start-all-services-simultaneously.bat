@echo off
echo ========================================
echo Starting ALL T1 Java Microservices
echo ========================================
echo.

echo Building project first...
call mvnw clean package -DskipTests
if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Starting all services simultaneously...
echo ========================================

echo Starting Client MS on port 8081...
start "Client MS" cmd /k "cd client-ms && mvnw.cmd spring-boot:run"

echo Starting Account MS on port 8082...
start "Account MS" cmd /k "cd account-ms && mvnw.cmd spring-boot:run"

echo Starting Credit MS on port 8083...
start "Credit MS" cmd /k "cd credit-ms && mvnw.cmd spring-boot:run"

echo.
echo ========================================
echo All services are starting...
echo ========================================
echo.
echo Service URLs:
echo - Client MS: http://localhost:8081
echo - Account MS: http://localhost:8082
echo - Credit MS: http://localhost:8083
echo.
echo H2 Console URLs:
echo - Client MS: http://localhost:8081/h2-console
echo - Account MS: http://localhost:8082/h2-console
echo - Credit MS: http://localhost:8083/h2-console
echo.
echo Press any key to exit...
pause > nul

