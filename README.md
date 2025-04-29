# Proyecto Integrador

Este proyecto consiste en una aplicación web dividida en tres módulos principales: `AdminApp`, `EmpApp` y `comun`, desarrollados con Spring Boot.

## Descripción General

El proyecto parece ser un sistema de gestión empresarial que incluye funcionalidades para administradores y empleados.

* **`comun`**: Este módulo contiene las entidades JPA (como `Empleado`, `Departamento`, `UsuarioEmpleado`, `Administrador`, `Etiqueta`, etc.), repositorios, excepciones personalizadas, validaciones y componentes comunes utilizados por los otros módulos. Utiliza una base de datos MySQL configurada en `application.properties` y carga datos iniciales a través de `data.sql`.
* **`AdminApp`**: Una aplicación web para administradores (puerto 9090). Permite gestionar empleados (listar, ver detalles, bloquearlos) y departamentos (ver detalles). Incluye autenticación para administradores y utiliza Thymeleaf para las vistas, junto con Bootstrap y JavaScript para la interfaz de usuario y la interacción con la API REST.
* **`EmpApp`**: Una aplicación web para empleados (puerto 8080, según `application.properties`). Incluye funcionalidades como registro de usuarios, login con gestión de cookies y seguridad, registro de datos de empleados por pasos (personales, contacto, profesionales, económicos), y funcionalidades de etiquetado de empleados (masivo, individual, eliminación). Utiliza Thymeleaf, Bootstrap, y JavaScript para la interfaz y la lógica del frontend.

## Tecnologías Principales

* **Backend**: Java, Spring Boot, Spring Data JPA, Hibernate
* **Base de Datos**: MySQL
* **Frontend**: Thymeleaf, HTML, CSS, JavaScript, Bootstrap, jQuery
* **Construcción**: Gradle

## Configuración

### Base de Datos

Ambas aplicaciones (`AdminApp` y `EmpApp`) están configuradas para conectarse a una base de datos MySQL llamada `integradora` en `localhost`. Las credenciales por defecto son:

* **Usuario**: `root`
* **Contraseña**: `admin`

El esquema de la base de datos se crea automáticamente si no existe (`createDatabaseIfNotExist=true`) y la opción `ddl-auto` está configurada (actualmente comentada como `update` o `create-drop` en los archivos, pero `spring.sql.init.mode=always` en `EmpApp/application.properties` indica que se usan `data.sql` y `schema.sql`). Se cargan datos iniciales desde `comun/src/main/resources/data.sql`.

### Puertos

* `AdminApp`: se ejecuta en el puerto `9090`.
* `EmpApp`: se ejecuta en el puerto `8080`.

## Funcionalidades Clave

### Módulo `comun`

* Define las entidades principales (`Administrador`, `Departamento`, `Empleado`, `Etiqueta`, `UsuarioEmpleado`, `Pais`, `Genero`, `Especialidad`, etc.).
* Proporciona repositorios JPA para interactuar con la base de datos.
* Define excepciones personalizadas para manejar errores específicos de la aplicación.
* Incluye validaciones personalizadas (Email, Fechas, IBAN, Documentos, etc.).

### Módulo `AdminApp`

* **Autenticación**: Login para administradores.
* **Gestión de Empleados**:
    * Listado paginado y filtrado de empleados.
    * Vista detallada de empleados.
    * Bloqueo/Desbloqueo de empleados con selección de motivo.
* **Gestión de Departamentos**:
    * Vista detallada de departamentos.
* **API REST**: Endpoints para obtener datos de empleados y departamentos (`/empleados`, `/departamentos`).

### Módulo `EmpApp`

* **Autenticación**:
    * Registro de nuevos usuarios empleados.
    * Login de empleados con gestión de usuarios recordados (cookies).
    * Actualización de contraseña.
    * Gestión de intentos fallidos y bloqueo de cuentas.
* **Registro de Empleados**: Proceso guiado por pasos para dar de alta la información completa de un empleado.
* **Gestión de Empleados (Vista Empleado)**:
    * Visualización de detalles del empleado, incluyendo jefe y subordinados.
* **Gestión de Etiquetas**:
    * Asignación/creación de etiquetas a subordinados (individual o masivo).
    * Eliminación de etiquetas asignadas a subordinados.
* **API REST**: Endpoints para operaciones de empleados, departamentos y etiquetas (`/empleados`, `/departamentos`, `/etiquetas`).

## Ejecución

1.  Asegúrate de tener una instancia de MySQL en ejecución.
2.  Configura la conexión a la base de datos en los archivos `application.properties` de los módulos `AdminApp` y `EmpApp` (si es diferente a `localhost`, `root`, `admin`).
3.  Construye el proyecto usando Gradle (desde la raíz `ProyectoIntegrador`):
    ```bash
    ./gradlew build
    ```
4.  Ejecuta las aplicaciones (puedes hacerlo desde tu IDE o usando los JAR generados):
    * Para `AdminApp`: `java -jar AdminApp/build/libs/AdminApp-xxxx.jar`
    * Para `EmpApp`: `java -jar EmpApp/build/libs/EmpApp-xxxx.jar`
5.  Accede a las aplicaciones:
    * AdminApp: `http://localhost:9090/adminapp/login`
    * EmpApp: `http://localhost:8080/empapp/login`

*(Nota: Reemplaza `xxxx` con la versión correspondiente del JAR generado)*
