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