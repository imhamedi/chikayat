@echo off
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080') do (
    taskkill /F /PID %%a
)
echo Application backend arrêtée.
pause
