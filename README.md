# Calculadora Java Swing

## Archivos
- `Main.java`: inicia la aplicación.
- `InterfazCalculadora.java`: interfaz gráfica, botones, historial y eventos.
- `Calculadora.java`: operaciones matemáticas.

## Requisitos
- JDK instalado.
- Los tres archivos en la misma carpeta y sin declaraciones `package`.

## Ejecutar desde Visual Studio Code
1. Abre esta carpeta en VS Code.
2. Abre `Main.java`.
3. Ejecuta el método `main` con el botón Run.

También puedes compilar y ejecutar desde la terminal, dentro de la carpeta:

```bash
javac *.java
java Main
```

## Funciones
- Suma, resta, multiplicación y división.
- Operaciones consecutivas de izquierda a derecha.
- Decimales, porcentaje y cambio de signo.
- Borrar último carácter y limpiar.
- Historial de operaciones con opción para borrarlo.
- La ventana se puede redimensionar. El historial se oculta cuando la ventana es estrecha y aparece al ampliarla.

## Porcentaje
El botón `%` divide el número que aparece actualmente en el visor entre 100.
Por ejemplo, `250` y luego `%` muestra `2.5`.
