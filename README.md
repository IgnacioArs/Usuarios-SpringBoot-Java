# Usuarios-SpringBoot-Java
Microservicio SpringBoot 2.5.14 / Gradle hasta 7.4 / Java 8 u 11

# Microservicio de Registro y Consulta de Usuarios

Este proyecto es un microservicio desarrollado con **Spring Boot 2.5.14**, **Java 11** y **Gradle 7.4**, que permite el registro (`/sign-up`) y consulta (`/login`) de usuarios, cumpliendo con las especificaciones requeridas para evaluación técnica.

---

## 🛠 Tecnologías utilizadas

- Java 11
- Spring Boot 2.5.14
- Spring Web
- Spring Data JPA
- Spring Security (JWT)
- H2 Database (in-memory)
- Lombok
- JUnit 5
- Gradle 7.4

---

## 🚀 Instrucciones para construir y ejecutar el proyecto

### ✅ Requisitos previos

- Java 11
- Gradle 7.4 o compatible
- Git (opcional, para clonar el repo)

### 🔧 Construcción

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/IgnacioArs/Usuarios-SpringBoot-Java.git
   cd usuarios

### Compilar el proyecto:

- ./gradlew clean build

### ▶️ Ejecución

- gradlew.bat bootRun
- usar este JDBC:jdbc:h2:mem:usuariosdb, URL en el login H2 INTERFACE DE LA SIGUIENTE URL
- CASO DE GENERAR ARCHIVO PARA ALMACENAMIENTO DE DATOS PARA DB H2: jdbc:h2:file:~/testdb
- Esta es la interface H2: http://localhost:8080/h2-ui/
- Swagger Docs : http://localhost:8080/swagger-ui/index.html

### 🧪 Pruebas Unitarias
- Se utilizan pruebas con JUnit 5 para los servicios.

- Ejecutar pruebas con:
./gradlew test

Cobertura mínima del 80% en UserService.

### 📂 Estructura del Proyecto

src/
 └── main/
     ├── java/com/evaluacion/usuarios/
     │   ├── controller/
     │   ├── dto/
     │   ├── entity/
     │   ├── exception/
     │   ├── repository/
     │   ├── security/
     │   ├── service/
     │   └── config/
     └── resources/
         └── application.properties

### 🖼 Diagramas UML
Ver la carpeta docs/ para:

📌 Diagrama de Componentes

📌 Diagrama de Secuencia

### 📘 Licencia
MIT
