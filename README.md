# Proyecto Final - Aplicación Móvil Moodle

## 📱 Descripción General

Aplicación Android nativa desarrollada para la **Universidad de Gotitas del Saber** que proporciona una interfaz móvil para acceder a la plataforma Moodle. La app permite a estudiantes gestionar cursos, tareas, foros y su perfil académico directamente desde dispositivos móviles.

## 🎯 Características Principales

- **Autenticación OAuth 2.0**: Integración segura con Moodle mediante OAuth
- **Gestión de Cursos**: Visualización y navegación de cursos inscritos
- **Detalles de Curso**: Acceso a contenido, recursos y actividades de cada curso
- **Gestión de Tareas**: Ver, descargar y enviar trabajos académicos
- **Foros de Discusión**: Participar en discusiones académicas en tiempo real
- **Perfil de Usuario**: Visualizar y gestionar datos del perfil
- **Seguridad**: Almacenamiento seguro de credenciales con `androidx.security:security-crypto`

## 🏗️ Arquitectura

El proyecto sigue una **arquitectura de capas (3-tier architecture)**:

```
ec.edu.ug.proyectofinal/
├── CapaPresentacion/      # Capa de UI (Activities, Adapters)
│   ├── Adapters/
│   ├── MainActivity.java
│   ├── CursosActivity.java
│   ├── DetalleCursoActivity.java
│   ├── EnviarTareaActivity.java
│   ├── ForoActivity.java
│   ├── PerfilActivity.java
│   └── Recursos.java
├── CapaServicio/          # Capa de Lógica de Negocio
│   ├── MoodleRepository.java       # Repositorio principal
│   ├── MoodleApiService.java       # Interfaz Retrofit
│   ├── MoodleAuthManager.java      # Gestión de autenticación
│   ├── Listener/                   # Callbacks de API
│   └── Network/                    # Configuración de red
└── CapaDatos/             # Capa de Datos
    └── Models/
        ├── User.java
        ├── Cursos/
        ├── Tareas/
        ├── Foros/
        └── Recursos.java
```

### Descripción de Capas

| Capa | Responsabilidad |
|------|-----------------|
| **CapaPresentacion** | Interfaz de usuario, Activities, Adapters, interacción con el usuario |
| **CapaServicio** | Lógica de negocio, gestión de API, autenticación, repositorio de datos |
| **CapaDatos** | Modelos de datos, estructuras de respuesta de API |

## 🔧 Tecnologías y Dependencias

### Core Android
- **API Level**: 24 (Android 7.0) - 36 (Android 15)
- **Language**: Java 11
- **AndroidX**: AppCompat, Material Design, Constraint Layout

### Librerías Principales
- **Retrofit 3.0.0**: Cliente HTTP para comunicación con API REST
- **Gson**: Serialización/deserialización de JSON
- **Chrome Custom Tabs**: Navegación segura para OAuth
- **Security Crypto**: Almacenamiento seguro de credenciales

### Testing
- **JUnit**: Tests unitarios
- **Espresso**: Tests de UI
- **AndroidJUnitRunner**: Test runner de Android

## 📋 Actividades Principales

### MainActivity
- Punto de entrada de la aplicación
- Maneja la autenticación OAuth con Moodle
- Implementa deep linking con esquema `miapp://`
- Recupera tokens de autenticación desde intent extras

### CursosActivity
- Lista todos los cursos del usuario
- Muestra cursos de estudiante y docente

### DetalleCursoActivity
- Visualiza detalles específicos de un curso
- Acceso a recursos y actividades

### EnviarTareaActivity
- Permite enviar tareas/trabajos
- Gestión de archivos adjuntos

### ForoActivity
- Visualización de foros del curso
- Creación y respuesta de discusiones

### PerfilActivity
- Datos del usuario autenticado
- Gestión de preferencias

## 🌐 Integración con Moodle

La aplicación se conecta a un servidor Moodle en:
```
https://192.168.100.5.nip.io/admin/tool/mobile/launch.php
```

### Flujo de Autenticación OAuth
1. Usuario inicia sesión
2. App redirige a Chrome Custom Tabs para autenticación en Moodle
3. Moodle devuelve token al esquema `miapp://`
4. App captura el token mediante deep linking
5. Token se almacena de forma segura con Security Crypto

### Endpoints API Utilizados
- `core_webservice_get_site_info`: Información del usuario
- `core_enrol_get_users_courses`: Cursos del usuario
- `core_course_get_contents`: Contenido del curso
- APIs de tareas, foros y recursos

## 📦 Estructura de Recursos

```
res/
├── layout/            # Layouts XML de actividades
├── drawable/          # Recursos gráficos
├── values/            # Strings, colores, estilos
├── values-night/      # Temas oscuros
├── mipmap-*/          # Iconos de aplicación
└── xml/               # Configuraciones (network security config, backup rules)
```

## 🔐 Configuración de Seguridad

- **Network Security Config**: Define políticas de comunicación HTTPS
- **Backup Rules**: Controla qué datos se respaldan
- **Data Extraction Rules**: Especifica qué datos pueden ser extraídos
- **Crypto Storage**: Almacenamiento seguro de tokens de autenticación

## 📋 Permisos Requeridos

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

## 🚀 Cómo Compilar y Ejecutar

### Requisitos
- Android Studio Arctic Fox o superior
- JDK 11 o superior
- Gradle 8.x

### Pasos
1. Clonar o descargar el repositorio
2. Abrir en Android Studio
3. Sincronizar Gradle (Build > Clean Project)
4. Ejecutar en emulador o dispositivo físico:
   ```bash
   ./gradlew installDebug
   ```

### Configuración Local
- Ajustar `moodleUrl` en `MainActivity.java` según tu servidor Moodle
- Configurar certificados SSL si es necesario en `network_security_config.xml`

## 🧪 Testing

Ejecutar tests:
```bash
# Tests unitarios
./gradlew test

# Tests instrumentados (Android)
./gradlew connectedAndroidTest
```

## 📱 Versiones Soportadas

| Versión | Descripción |
|---------|-------------|
| **Min SDK** | 24 (Android 7.0) |
| **Target SDK** | 36 (Android 15) |
| **Version Code** | 1 |
| **Version Name** | 1.0 |

## 📊 Modelos de Datos

La aplicación maneja los siguientes modelos:

- **User**: Información del usuario autenticado
- **UserCourse**: Cursos del usuario
- **TeacherCourseResponse**: Respuesta de cursos para docentes
- **Tarea/Assignment**: Información de tareas
- **AssignmentConfig**: Configuración de tareas
- **UploadResponse**: Respuesta de carga de archivos
- **Foros**: Información de foros
- **ForoResultados/NuevaDiscusion**: Datos de discusiones
- **Recursos**: Recursos del curso

## 🤝 Contribución

Este es un proyecto académico de la Universidad de Guayaquil. Para cambios:
1. Crear una rama con tu feature
2. Hacer commit de cambios
3. Push a la rama
4. Abrir un Pull Request

## 📄 Licencia

Proyecto académico de la Universidad de Guayaquil (8to Semestre)

## 👨‍💻 Información del Proyecto

- **Organización**: ec.edu.ug.proyectofinal
- **Plataforma**: Android
- **Estado**: En desarrollo
