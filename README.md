# Mi Casa Ya - Aplicación Android

**Mi Casa Ya** es una aplicación móvil desarrollada en Android Studio con Java. Su propósito es ayudar a personas interesadas en construir su casa propia, brindándoles una estimación básica de los elementos estructurales que necesitarían, de forma sencilla, rápida y educativa.

## Características principales

- Registro e inicio de sesión de usuarios
- Calculadora estructural:
  - Cálculo del número aproximado de columnas
  - Cálculo de vigas horizontales y verticales
  - Recomendación del tipo de cimentación (zapata aislada o corrida)
  - Recomendación automatizada usando IA (Gemini)
- Visualización gráfica simple de la estructura
- Interfaz intuitiva y amigable
- Código 100% en Java

## ¿Cómo funciona la calculadora?

El usuario debe ingresar los siguientes datos:

- Largo del terreno (m)
- Ancho del terreno (m)
- Número de habitaciones
- Número de pisos

Con estos datos, la app calcula:

- Número de columnas (con separación promedio de 3 metros)
- Número de vigas necesarias
- Tipo de cimentación sugerida (basada en la cantidad de pisos)
- Recomendación adicional generada mediante una solicitud a una IA (opcional)

## Tecnologías utilizadas

- Android Studio
- Lenguaje Java
- Maps api (para georeferenciar el proyecto
- Gemini API (para recomendaciones)
- Vista personalizada para representar planos estructurales

## Cómo compilar el proyecto

1. Clona o descarga este repositorio en tu equipo.
2. Abre el proyecto en Android Studio.
3. Conecta un dispositivo físico o ejecuta un emulador.
4. Haz clic en el botón "Run" (▶) en Android Studio.

## Requisitos

- Android Studio Electric Eel o superior
- SDK mínimo: API 21 (Android 5.0 Lollipop)
- Conexión a internet para usar Gemini (opcional)

## Licencia

Este proyecto fue desarrollado con fines académicos. Puedes usarlo, modificarlo y compartirlo libremente para fines educativos.

Desarrollado por estudiantes de Ingeniería Civil.
