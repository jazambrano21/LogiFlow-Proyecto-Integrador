# LogiFlow - Proyecto Integrador
##ENDPOINTS Y EJECUCION

## Configuración del pipeline

Este repositorio usa una GitHub Action definida en `.github/workflows/build.yml`.

La configuración del pipeline incluye:
- Desencadenadores en `push` y `pull_request` para las ramas `main` y `development`.
- Preparación del entorno con JDK 17 usando `actions/setup-java@v4`.
- Caché para dependencias Maven y archivos de SonarCloud.
- Ejecución de `mvn -B verify sonar:sonar` dentro de `Backend/ms-autenticacion`.
- Análisis de calidad en SonarCloud con el proyecto `jazambrano21_LogiFlow-Proyecto-Integrador`.
- Notificaciones por Telegram cuando la compilación termina en éxito o fallo.

Esta configuración permite verificar el código, ejecutar pruebas y controlar la calidad en cada cambio relevante del proyecto.

