# Los cinco filósofos

Simulación en Java del problema clásico de los cinco filósofos comensales. El proyecto recorre, paso a paso, los problemas de concurrencia que aparecen cuando varios hilos compiten por recursos compartidos: desde la ausencia total de control hasta la detección y resolución de deadlocks.

> **Asignatura:** Programación de Servicios y Procesos (PSP) · DAM 2º  
> **Autores:** Miguel De la Iglesia Gil · Alejandro De Prado  
> **Docente:** Daniel Jaén

---

## Estructura del proyecto

```
CarreraHilos/src/ejercicioFilosofos/
├── reto1/          → Simulación funcional y configurable
├── reto2/          → Race condition demostrada (sin sincronización)
├── reto3/          → Race condition resuelta (synchronized + wait/notify)
├── reto4/          → Deadlock provocado y detectado
├── reto5/
│   ├── solucionA/  → Deadlock resuelto: filósofo asimétrico
│   └── solucionB/  → Deadlock resuelto: ControlAcceso (máximo N-1)
├── presentacion.html   → Presentación de diapositivas (abrir en navegador)
└── explicacion_*.md    → Notas explicativas de cada reto
```

---

## Retos

| Reto | Concepto | Lo que demuestra |
|---|---|---|
| Reto 1 | Simulación base | 5 hilos compartiendo 5 tenedores, estados visibles en consola |
| Reto 2 | Race condition | Sin `synchronized`, los filósofos cogen el mismo tenedor a la vez |
| Reto 3 | Sincronización | `synchronized` + `wait/notify` elimina la race condition |
| Reto 4 | Deadlock | `tiempoPensando = 0` → todos en `WAITING` en segundos |
| Reto 5A | Solución asimétrica | F4 coge los tenedores al revés → rompe la espera circular |
| Reto 5B | ControlAcceso | Máximo N-1 filósofos intentando comer → siempre hay hueco |
| Reto extra | Equidad | Análisis min/max/diferencia de comidas tras 30 segundos |

---

## Cómo ejecutar

Cada reto tiene su propio `Main.java`. Basta con ejecutarlo desde el IDE o desde línea de comandos:

```bash
# Desde la raíz del proyecto
javac -d out CarreraHilos/src/ejercicioFilosofos/reto1/*.java
java -cp out ejercicioFilosofos.reto1.Main
```

Los parámetros de simulación (número de filósofos, tiempos, duración) están como variables al principio de cada `Main.java`.

---

## Restricciones técnicas

- Solo se usan mecanismos de la unidad: `Thread`, `synchronized`, `wait`, `notify` y variables atómicas.
- Prohibido `java.util.concurrent.locks` y utilidades de alto nivel de concurrencia.

---

