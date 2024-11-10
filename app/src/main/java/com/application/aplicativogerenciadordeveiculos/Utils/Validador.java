package com.application.aplicativogerenciadordeveiculos.Utils;

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
}
