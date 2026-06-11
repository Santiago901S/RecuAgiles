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


## 3. Pruebas Unitarias (UT4 - JUnit 5)
Para garantizar la estabilidad de la lógica de negocio del simulador, se han implementado pruebas unitarias automatizadas empleando el framework JUnit 5.

### Cómo ejecutar las pruebas:
1. En la vista del proyecto de Eclipse, hacer clic derecho sobre la clase `ControladorJuegoTest.java`.
2. Seleccionar la opción `Run As` ➡️ `JUnit Test`.

### Salida de la ejecución de Tests (Resultado):
- **Tests run:** 2/2 (100% exitosos)
- **Errors:** 0
- **Failures:** 0
- **Resultado Visual:** Barra de estado en verde (Green Bar), lo que certifica la correcta ejecución del flujo lógico verificado sin excepciones.

## 4. Reporte de Calidad de Código (CheckStyle)
### Cómo ejecutar CheckStyle:
1. Clic derecho sobre el proyecto raíz `FutbolProyect`.
2. Seleccionar `Checkstyle` ➡️ `Check Code with Checkstyle`.

### Salida de la herramienta:
El linter resalta interactivamente en el editor de código las líneas afectadas mediante marcadores amarillos. Los fallos principales detectados incluyen:
- Ausencia de bloques de comentarios Javadoc estandarizados en métodos internos.
- Ubicación incorrecta de llaves de apertura `{` según las convenciones Java de Sun.

## 🛠️ Instrucciones de Ejecución por Terminal (UT4)

### Cómo ejecutar los Tests Unitarios:
Para lanzar las pruebas unitarias automatizadas con JUnit desde la consola, colócate en la raíz del proyecto y ejecuta:
```bash
mvn test


## 🛠️ Pruebas Unitarias y Calidad de Código (UT4)

### Comando teórico de ejecución de Tests:
`mvn test`

#### Resultado obtenido (Salida de JUnit en Eclipse):
```text
[Haz clic abajo en la pestaña 'Console' de Eclipse después de hacer Run As -> JUnit Test, copia todo ese texto y pégalo aquí]
		
Description	Resource	Path	Location	Type
Plugin execution not covered by lifecycle configuration: org.apache.maven.plugins:maven-checkstyle-plugin:3.3.1:check (execution: validate, phase: validate)	pom.xml	/FutbolProyect	line 56	Maven Project Build Lifecycle Mapping Problem

Description	Resource	Path	Location	Type
',' no está seguido de espacio en blanco.	PanelVictoria.java	/FutbolProyect/src/vista	line 361	Checkstyle Problem

Aqui vemos los errores, que hay y avisos con el checkstyle.


