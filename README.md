# UT3 y UT4 - Calidad del Software y Automatización

## 1. Configuración de Herramientas
- **Gestión del Proyecto:** Se ha convertido la aplicación a un proyecto gestionado por Maven para estandarizar el ciclo de vida del software.
- **Auditoría de Calidad:** Se ha integrado el linter `Checkstyle` para analizar automáticamente el formato, el estilo y las convenciones del código Java.

## 2. Informe de Ejecución de Checkstyle
Al ejecutar la herramienta sobre el código actual (como el archivo `ControladorJuego.java`), el analizador ha reportado diversas alertas de estilo:

- **Estructura y Llaves:** Avisos de llaves de apertura `{` fuera de la posición estándar (deben seguir la convención de Sun de ir en la línea anterior).
- **Longitud de Líneas:** Advertencias en líneas extensas que superan el límite estándar de caracteres permitidos por línea.
- **Documentación:** Alertas por ausencia de comentarios Javadoc en métodos clave de lógica interna.

*Nota: Todas las incidencias se han visualizado de forma automatizada e interactiva dentro del entorno de desarrollo (IDE) para agilizar las tareas de refactorización y garantizar un desarrollo ágil.*