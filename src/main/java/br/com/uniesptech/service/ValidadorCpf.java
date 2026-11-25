package br.com.uniesptech.service;

/**
 * Valida CPF pelos dígitos verificadores (algoritmo oficial da Receita Federal).
 * Aceita o CPF com ou sem pontuação.
 */
public final class ValidadorCpf {

    private ValidadorCpf() {
    }

    /** Remove tudo que não for dígito. */
    public static String normalizar(String cpf) {
        return cpf == null ? "" : cpf.replaceAll("\\D", "");
    }

    public static boolean ehValido(String cpf) {
        String digitos = normalizar(cpf);
        if (digitos.length() != 11) {
            return false;
        }
        // CPFs com todos os dígitos iguais (111.111.111-11 etc.) passam na conta, mas são inválidos.
        if (digitos.chars().distinct().count() == 1) {
            return false;
        }
        return digitoVerificador(digitos, 9) == digitos.charAt(9) - '0'
                && digitoVerificador(digitos, 10) == digitos.charAt(10) - '0';
    }

    private static int digitoVerificador(String digitos, int quantidade) {
        int soma = 0;
        for (int i = 0; i < quantidade; i++) {
            soma += (digitos.charAt(i) - '0') * (quantidade + 1 - i);
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}
