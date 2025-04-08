# MyTasks

**MyTasks** es una aplicación Android nativa desarrollada en **Kotlin** con **Jetpack Compose**, que permite gestionar tareas personales de manera sencilla, rápida y eficiente. La aplicación implementa funcionalidades clave como creación, listado, marcado como completado, eliminación con opción a deshacer y persistencia local usando **Room**. La interfaz está diseñada para ofrecer una experiencia de usuario fluida con soporte para agrupación de tareas por fecha y animaciones suaves.

## Características principales

- Listado de tareas con **agrupación por fecha**
- **Creación de nuevas tareas** con campos de título, descripción y prioridad
- Posibilidad de **marcar tareas como completadas**
- Eliminación de tareas mediante **Swipe-to-Dismiss**, con opción de **deshacer** desde Snackbar
- **Persistencia local** usando Room Database
- **Paginación** local para mejorar el rendimiento en listas extensas
- Arquitectura modular siguiendo principios de **Clean Architecture**
- Gestión de estados y eventos con **StateFlow**, **LiveData**, y **ViewModel**
- UI completamente construida con **Jetpack Compose**

## Tecnologías utilizadas

- **Lenguaje:** Kotlin
- **UI:** Jetpack Compose
- **Base de datos:** Room
- **Inyección de dependencias:** Hilt
- **Arquitectura:** MVVM + Clean Architecture
- **Corrutinas:** Kotlin Coroutines + Flow
- **Testing:** (Espacio reservado para futuras implementaciones)

## Arquitectura

La aplicación sigue una estructura basada en Clean Architecture:

