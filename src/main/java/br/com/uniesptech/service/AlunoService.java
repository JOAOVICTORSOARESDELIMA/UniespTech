package br.com.uniesptech.service;

import br.com.uniesptech.model.Aluno;
import br.com.uniesptech.repository.AlunoRepository;
import java.util.List;
import java.util.logging.Logger;

/** Regras de negócio do cadastro de alunos. */
public class AlunoService {

    private static final Logger LOG = Logger.getLogger(AlunoService.class.getName());
    static final int TAMANHO_MAXIMO_NOME = 120;

    private final AlunoRepository repository;

    public AlunoService(AlunoRepository repository) {
        this.repository = repository;
    }

    public Aluno cadastrar(String nome, String cpf) {
        String nomeLimpo = nome == null ? "" : nome.trim().replaceAll("\\s+", " ");
        String cpfLimpo = ValidadorCpf.normalizar(cpf);

        if (nomeLimpo.isEmpty()) {
            throw rejeitar("O nome não pode ser vazio.");
        }
        if (nomeLimpo.length() > TAMANHO_MAXIMO_NOME) {
            throw rejeitar("O nome pode ter no máximo " + TAMANHO_MAXIMO_NOME + " caracteres.");
        }
        if (cpfLimpo.length() != 11) {
            throw rejeitar("CPF inválido: deve ter 11 dígitos.");
        }
        if (!ValidadorCpf.ehValido(cpfLimpo)) {
            throw rejeitar("CPF inválido: os dígitos verificadores não conferem.");
        }
        if (repository.buscarPorCpf(cpfLimpo).isPresent()) {
            throw rejeitar("Já existe um aluno cadastrado com este CPF.");
        }

        Aluno aluno = repository.salvar(nomeLimpo, cpfLimpo);
        LOG.info(() -> "Aluno cadastrado: id=" + aluno.id());
        return aluno;
    }

    public List<Aluno> listar() {
        return repository.listarTodos();
    }

    /** Apaga todos os alunos e devolve quantos foram apagados. */
    public int deletarTodos() {
        int quantidade = repository.listarTodos().size();
        repository.deletarTodos();
        LOG.warning(() -> "Todos os alunos foram apagados (" + quantidade + " registro(s)).");
        return quantidade;
    }

    private static ValidacaoException rejeitar(String motivo) {
        // O CPF não vai para o log: é dado pessoal (LGPD).
        LOG.info(() -> "Cadastro recusado: " + motivo);
        return new ValidacaoException(motivo);
    }
}
