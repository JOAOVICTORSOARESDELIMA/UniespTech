package br.com.uniesptech.service;

/** Erro de regra de negócio, com mensagem pronta para mostrar ao usuário. */
public class ValidacaoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
}
