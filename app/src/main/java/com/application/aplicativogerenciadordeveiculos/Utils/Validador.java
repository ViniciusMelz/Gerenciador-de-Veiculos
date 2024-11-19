package com.application.aplicativogerenciadordeveiculos.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validador {
    public static boolean validaTexto(String texto) {
        boolean resultado;
        if (!texto.isEmpty()) {
            resultado = true;
        } else {
            resultado = false;
        }
        return resultado;
    }

    public static boolean validaEmail(String email) {
        boolean resultado;
        if (!email.isEmpty() && email.contains("@") && email.contains(".")) {
            resultado = true;
        } else {
            resultado = false;
        }
        return resultado;
    }

    public static boolean validaSenha(String senha) {
        boolean resultado;
        if (senha.length() >= 6) {
            resultado = true;
        } else {
            resultado = false;
        }
        return resultado;
    }

    public static boolean ehDataValida(String data) {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        try {
            LocalDateTime.parse(data, formatador);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
