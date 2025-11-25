<img width="100%" src="https://capsule-render.vercel.app/api?type=waving&color=9df2ea&animation=fadeIn&height=120&section=header"/>

# 💰 Personal Finance Manager
Gestor de finanzas personales dockerizado. Construido desde cero aplicando buenas prácticas. 


![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Clean Architecture](https://img.shields.io/badge/Architecture-Clean-orange)
![Hexagonal](https://img.shields.io/badge/Hexagonal-Design-blueviolet)
![CQRS](https://img.shields.io/badge/CQRS-Pattern-9cf)
![DDD](https://img.shields.io/badge/Domain%20Driven%20Design-Strategic-purple)

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.x-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-5-25A162?logo=junit5&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Building-C71A36?logo=apachemaven)

![Status](https://img.shields.io/badge/Status-Active-success)
![Swagger](https://img.shields.io/badge/Docs-Swagger%20UI-85EA2D?logo=swagger&logoColor=black)
![OpenAPI](https://img.shields.io/badge/OpenAPI-3.1-6BA539)
![Tests](https://img.shields.io/badge/Tests-Passing-brightgreen)
![Mockito](https://img.shields.io/badge/Mockito-Testing-00C853?logo=mockito)



---

## 🚀 Características principales

- CRUD completo de **Accounts**, **Transactions** y **Categories**
- Arquitectura limpia: *Clean Architecture + Hexagonal*
- Separación clara entre comandos y consultas (*CQRS*)
- Dominio robusto con invariantes (ej: Category no puede borrarse si tiene transactions)
- Persistencia real con **PostgreSQL 16**
- Entorno de desarrollo fácil con **Docker Compose**
- Pruebas automáticas con **JUnit5 + Mockito**
- Documentación automática con **Swagger / OpenAPI**
- Preparado para añadir frontend y CI/CD

---

## 📦 Estructura del Proyecto

```
personal-finance-manager/
│
├── backend/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/dev.jossegonnza.personal_finance_manager/
│       ├── api/              → Controladores REST, DTOs, responses, filtros
│       ├── application/
│       │   ├── command/      → Casos de uso que modifican el estado
│       │   └── query/        → Casos de uso que leen datos
│       ├── domain/           → Entidades, Value Objects, reglas, invariantes
│       └── infrastructure/   → Adaptadores JPA, configuración de persistencia
│
├── frontend/ (vacío por ahora)
│
└── docker-compose.yml
```

---

## 🧠 Arquitectura

```
[ REST Controllers ]
        |
        |   Validan → Mapean DTOs → Delegan
        v
[ Command Use Cases ]     [ Query Use Cases ]
        |                      |
        |______________________|
        |
[ Domain Layer ]
Entities, Value Objects (Money), reglas e invariantes
        |
[ JPA Adapters ]
Repositorios, mapeo ORM
        |
[ PostgreSQL Database ]
```

✔ **Bajo acoplamiento**  
✔ **Alta testabilidad**  
✔ **Dominio consistente y expresivo**  

---

## 🧪 Tests

```bash

mvn clean test

```

---

## 🗄️ Base de datos

Actualmente, el proyecto utiliza **PostgreSQL 16** en Docker.


### Reglas del dominio importantes:

- ❌ No se puede borrar una categoria con transactions (invariante)
- ✔ Las transactions actualizan la lógica de balance en Account
- ✔ Money es un Value Object seguro (sin floats)

---

## 🐳 Docker

### Levantar todo:

```bash

docker compose up -d
```

---

## ▶️ Ejecutar el backend sin Docker

```bash

cd backend
mvn spring-boot:run
```

### Endpoints disponibles

| Recurso     | URL |
|-------------|-----|
| Swagger UI  | http://localhost:8080/swagger-ui.html |
| API Docs    | http://localhost:8080/v3/api-docs |
| H2 Console  | *(solo modo h2, desactivado en Docker)* |

---

## 🔧 Tecnologías Principales

- Java 21
- Spring Boot 3.5.7
- Spring Data JPA
- PostgreSQL 16
- Docker + Docker Compose
- JUnit5 + Mockito
- SpringDoc OpenAPI
- Arquitectura clean + CQRS + Hexagonal

---

## 🌱 Próximos Pasos

- [ ] Dashboard con estadísticas
- [ ] Análisis mensual automático
- [ ] Pipeline CI/CD con GitHub Actions
- [ ] Añadir `frontend/` con React o Next.js
- [ ] Añadir autenticación JWT

---
![Author](https://img.shields.io/badge/Author-Jose%20Gonnza-blue)
<img src="https://raw.githubusercontent.com/matfantinel/matfantinel/master/waves.svg" width="100%" height="100">