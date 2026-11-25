package br.com.uniesptech.model;

import java.util.Objects;

/** Aluno cadastrado no sistema. O CPF é guardado só com os 11 dígitos. */
public record Aluno(long id, String nome, String cpf) {

    public Aluno {
        Objects.requireNonNull(nome, "nome");
        Objects.requireNonNull(cpf, "cpf");
    }

    /** CPF no formato 000.000.000-00, para exibição. */
    public String cpfFormatado() {
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "."
                + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }
}
