package com.github.nullsafe;

import com.github.nullsafe.adapters.NullSafeAdapters;
import java.util.Optional;

public class NullsafeApp {

    public static void main(String[] args) {
     
        NullSafe<String> input = NullSafe.of("  hola mundo  ");

        String result2 = input
                .toResult("Entrada vacía")
                .map(String::trim)
                .map(s -> s.substring(0, 5))
                .map(String::toUpperCase)
                .recover(err -> "VALOR POR DEFECTO")
                .get();

        System.out.println(result2); // Imprime: HOLA M
    }
}
