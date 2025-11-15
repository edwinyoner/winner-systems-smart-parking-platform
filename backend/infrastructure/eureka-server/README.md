# Eureka Server - Smart Parking Platform

Netflix Eureka Server para Service Discovery.

## Prerequisitos

- Config Serveer debe estar corriendo en puerto 8888

## Ejecutar
```
mvn spring-boot:run
```

## Verificar

- Eureka Dashboard: http://localhost:8761
- Health: http://localhost:8761/actuator/health
- Eureka Apps: http://localhost:876/eureka/apps

## Dashbooard

El Dashboard de Eureka muestra:
- Servicios registrados
- Instancias disponibles
- Estado de salud
- Renovacioes y desalojos
