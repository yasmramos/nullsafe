# NullSafe - Manejo Seguro de Valores Nulos en Java

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/yasmramos/nullsafe) 
[![License](https://img.shields.io/github/license/tu-usuario/nullsafe)](https://github.com/yasmramos/nullsafe) 
[![GitHub stars](https://img.shields.io/github/stars/tu-usuario/nullsafe?style=social)](https://github.com/yasmramos/nullsafe)

> Una alternativa más potente que `Optional<T>` para manejar valores nulos de forma segura, limpia y funcional en Java.

---

## 📌 ¿Qué es NullSafe? 

`NullSafe<T>` es una clase diseñada para encapsular valores que pueden ser `null`, ofreciendo métodos encadenables, validaciones, recuperación de errores, adaptadores funcionales y soporte para colecciones, mapas y cadenas.

Evita los temidos `NullPointerException` y ofrece una API rica y flexible para desarrollar código limpio, seguro y expresivo.

---

## ✨ Características Destacadas

| Característica | Descripción |
|----------------|-------------|
| ✅ Encadenamiento funcional | Métodos como `map`, `flatMap`, `filter`, etc. |
| ✅ Recuperación de fallos | `.recover(...)`, `.recoverWith(...)` |
| ✅ Validaciones integradas | `.validate(pred, msg)`, `.validate(pred, () -> ex)` |
| ✅ Conversión a tipos | `.toOptional()`, `.toResult("error")`, `.stream()` |
| ✅ Adaptadores personalizados | `.adapt(adapter)` |
| ✅ Logging integrado | `.logIfPresent(...)`, `.logIfAbsent(...)` |
| ✅ Combinación de múltiples valores | `.combine(a, b, (x, y) -> x + y)` |
| ✅ Uso en colecciones y mapas | Métodos estáticos como `filterNonNull(...)`, `mapNonNullValues(...)` |
| ✅ Tipos primitivos especializados | `NullSafeInt`, `NullSafeLong`, `NullSafeDouble`, `NullSafeFloat`, `NullSafeBoolean`, `NullSafeByte`, `NullSafeShort` |

---

## 💡 Ejemplo Rápido

```java
NullSafe.of("   hola mundo   ")
        .map(String::trim)
        .filter(s -> s.length() > 5)
        .map(String::toUpperCase)
        .recover(ex -> "VALOR POR DEFECTO")
        .validate(s -> s.contains("HOLA"), "No contiene 'HOLA'")
        .ifPresent(System.out::println);
```

## 🧩 Integración con Result<T, E> 

Puedes convertir fácilmente entre NullSafe<T> y Result<T, E>:

```java
 
Result<String, String> result = NullSafe.of("valor").toResult("Valor no encontrado");

result.ifSuccess(System.out::println)
       .ifFailure(err -> System.err.println("Error: " + err));
```

## 🔢 Tipos Primitivos NullSafe

Además de `NullSafe<T>`, la librería incluye clases especializadas para manejar tipos primitivos de forma segura:

- **`NullSafeInt`** - Manejo seguro de valores `Integer`
- **`NullSafeLong`** - Manejo seguro de valores `Long`
- **`NullSafeDouble`** - Manejo seguro de valores `Double`
- **`NullSafeFloat`** - Manejo seguro de valores `Float`
- **`NullSafeBoolean`** - Manejo seguro de valores `Boolean`
- **`NullSafeByte`** - Manejo seguro de valores `Byte`
- **`NullSafeShort`** - Manejo seguro de valores `Short`

### Ejemplos de Uso con Tipos Primitivos

```java
// NullSafeInt
int result = NullSafeInt.of(42)
    .ifPresent(value -> System.out.println("Valor: " + value))
    .orElse(0);

// NullSafeDouble con validaciones
double price = NullSafeDouble.of(product.getPrice())
    .validate(p -> p > 0, "El precio debe ser positivo")
    .orElse(0.0);

// NullSafeBoolean para lógica condicional
boolean isValid = NullSafeBoolean.of(userInput)
    .orElse(false);

// Manejo de valores extremos
NullSafeLong.of(Long.MAX_VALUE)
    .orElseThrow(() -> new RuntimeException("Valor muy grande"));

NullSafeDouble.of(Double.NaN)
    .ifPresent(value -> System.out.println("Es NaN: " + value))
    .orElseGet(() -> 0.0);
```

### Ventajas de los Tipos Primitivos

- **Rendimiento**: Evitan el boxing/unboxing automático
- **Memoria**: Menor uso de memoria que los tipos boxing
- **Seguridad**: Misma protección contra `NullPointerException`
- **Consistencia**: Misma API que `NullSafe<T>` pero optimizada para primitivos
---

## 📦 Instalación

---

## 🧪 Pruebas Unitarias

Todas las funciones están cubiertas por pruebas unitarias usando JUnit 5 . Puedes ejecutarlas así:

```bash 
   mvn test
   ```

O si usas Gradle:

```bash
   gradle test
```
---

## 📊 Cobertura de Código

Soporta análisis de cobertura con JaCoCo :

```bash
   mvn clean test jacoco:report
```
El informe se genera en:
```text
   target/site/jacoco/index.html
```
---

## 🧱 Contribuir

¡Estamos abiertos a contribuciones! Si quieres mejorar la librería, corrige errores, añadir nuevas funciones o traducir documentación, ¡adelante! 

   1. Abre un issue
   2. Haz un fork
   3. Crea una nueva rama (git checkout -b feature/foo)
   4. Haz commit de tus cambios (git commit -m 'Add some foo')
   5. Sube los cambios (git push origin feature/foo)
   6. Crea un pull request


---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT .

© 2025 yasmramos / Comunidad Java.

---

## 📬 Contacto

Si tienes dudas, sugerencias o quieres colaborar: 

    📧 Email: yasmramos95@gmail.com 
    🐙 GitHub: @yasmramos 
     