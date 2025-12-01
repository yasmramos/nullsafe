#!/bin/bash

# Script para compilar el proyecto nullsafe paso a paso
# Útil para identificar y resolver errores específicos

echo "=== NULLSAFE COMPILATION HELPER ==="
echo "Compilando por módulos para resolver errores específicos..."

# 1. Clases principales (más estables)
echo "1. Compilando clases principales..."
cd /workspace/nullsafe
javac -cp "." src/main/java/com/github/nullsafe/NullSafe*.java
echo "Resultado: $?"

# 2. Collections (recientemente arregladas)
echo "2. Compilando collections..."
javac -cp "." src/main/java/com/github/nullsafe/collections/*.java 2>&1 | head -10
echo "Resultado compilación collections"

# 3. Validation (cambios recientes)
echo "3. Compilando validation..."
javac -cp "." src/main/java/com/github/nullsafe/validation/*.java
echo "Resultado: $?"

# 4. Async (con tipos genéricos complejos)
echo "4. Compilando async..."
javac -cp "." src/main/java/com/github/nullsafe/async/*.java 2>&1 | head -10
echo "Resultado compilación async"

# 5. Stream operations
echo "5. Compilando streams..."
javac -cp "." src/main/java/com/github/nullsafe/stream/*.java 2>&1 | head -10
echo "Resultado compilación streams"

# 6. Performance (menos crítico)
echo "6. Compilando performance..."
javac -cp "." src/main/java/com/github/nullsafe/performance/*.java
echo "Resultado: $?"

# 7. Compilación final con Maven
echo "7. Compilación final con Maven..."
mvn compile 2>&1 | tail -20

echo "=== COMPILATION SCRIPT COMPLETED ==="