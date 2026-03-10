package src.repository;

import src.model.Aluno;
import java.util.List;

public interface AlunoRepository {

    void salvar(Aluno aluno);
    List<Aluno> listarTodos();
    void deletarTodos();
    int proximoId();

}
