# API Gateway - Smart Parking Platform
spring Cloud Gateway para enrutamiento y balanceo de carga.

## Prerequisitos

- Config Server corriendo en puerto 8888
- Eureka Server corriendo en puerto 8761 

## Ejecutar
```
mvn spring-boot:run
```

## Verificar

- `/api/auth/**` -> Auth Service
- `/api/users/**` -> User Service

## CORS

Configurando para permitir requests desde:
- http://localhost:4200 (Angular)
- http://localhost:3000 (Flutter)