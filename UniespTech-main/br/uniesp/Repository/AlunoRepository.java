package br.uniesp.Repository;

import java.util.ArrayList;
import br.uniesp.Model.Aluno;

public class AlunoRepository {
    private ArrayList<Aluno> alunos =  new ArrayList<>();
    public void cadastrar (Aluno aluno) {
        alunos.add(aluno);
    }

    public ArrayList<Aluno> listar() {
        return alunos;
    }
    public void deletartodos() {
        alunos.clear();
    }
}
