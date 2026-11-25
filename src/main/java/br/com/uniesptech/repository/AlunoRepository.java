package br.com.uniesptech.repository;

import br.com.uniesptech.model.Aluno;
import java.util.List;
import java.util.Optional;

/** Acesso aos dados dos alunos. A camada de serviço não sabe onde os dados ficam. */
public interface AlunoRepository {

    /** Salva um novo aluno e devolve o registro com o id gerado. */
    Aluno salvar(String nome, String cpf);

    List<Aluno> listarTodos();

    Optional<Aluno> buscarPorCpf(String cpf);

    void deletarTodos();

    /** Verifica se o armazenamento está acessível (usado pelo health check). */
    boolean estaDisponivel();
}
