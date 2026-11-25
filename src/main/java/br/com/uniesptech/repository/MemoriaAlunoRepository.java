package br.com.uniesptech.repository;

import br.com.uniesptech.model.Aluno;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Repositório em memória, usado nos testes. Os dados somem ao encerrar. */
public class MemoriaAlunoRepository implements AlunoRepository {

    private final List<Aluno> alunos = new ArrayList<>();
    private long proximoId = 1;

    @Override
    public synchronized Aluno salvar(String nome, String cpf) {
        Aluno aluno = new Aluno(proximoId++, nome, cpf);
        alunos.add(aluno);
        return aluno;
    }

    @Override
    public synchronized List<Aluno> listarTodos() {
        return List.copyOf(alunos);
    }

    @Override
    public synchronized Optional<Aluno> buscarPorCpf(String cpf) {
        return alunos.stream().filter(a -> a.cpf().equals(cpf)).findFirst();
    }

    @Override
    public synchronized void deletarTodos() {
        alunos.clear();
    }

    @Override
    public boolean estaDisponivel() {
        return true;
    }
}
