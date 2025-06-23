package com.github.nullsafe.adapters;

import java.util.function.Supplier;

public class NullSafeAdapters {

    /**
     * Devuelve un adaptador que recorta espacios de una cadena si está
     * presente.
     */
    public static NullSafeAdapter<String, String> trimString() {
        return ns -> ns.map(String::trim);
    }

    /**
     * Devuelve un adaptador que convierte una cadena a mayúsculas si no es
     * null.
     */
    public static NullSafeAdapter<String, String> toUpperCaseIfPresent() {
        return ns -> ns.map(String::toUpperCase);
    }

    /**
     * Devuelve un adaptador que convierte una cadena a minúsculas si no es
     * null.
     */
    public static NullSafeAdapter<String, String> toLowerCaseIfPresent() {
        return ns -> ns.map(String::toLowerCase);
    }

    /**
     * Devuelve un adaptador que filtra valores mayores a cero (para números).
     */
    public static NullSafeAdapter<Integer, Integer> filterPositive() {
        return ns -> ns.filter(v -> v > 0);
    }

    /**
     * Devuelve un adaptador que convierte un objeto en su representación JSON
     * opcional.
     */
    public static NullSafeAdapter<Object, String> toJson(Supplier<String> serializer) {
        return ns -> ns.map(v -> serializer.get());
    }
}
