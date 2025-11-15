# Config Server - Smart Parking Platform

Spring cloud Config Server centralizado para todos los microservicios

## Ejecutar
```
mvn spring-boot:run
```

## Verificar
- Config Server: http://localhost:8888
- Health: http://localhost:8888/actuator/health

## Obtener configuración de un servicio
```
curl http://localhost:8888/auth-service/default
curl http://localhost:8888/eureka-server/default
```

## Archivos de configuración

Los archivos están en: `src/main/resources/config/ls` 