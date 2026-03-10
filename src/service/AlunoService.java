package src.service;

import src.model.Aluno;
import src.repository.AlunoRepository;

import java.util.List;

public class AlunoService {

    private final AlunoRepository repository;

    public AlunoService(AlunoRepository repository) {
        this.repository = repository;
    }

    public String cadastrar(String nome, String cpf){

        if (nome == null || nome.trim().isEmpty()){
            return "ERROR: Nome não pode ser vazio!";
        }
        if (cpf == null || cpf.length() != 11 || cpf.matches("\\d+")) {
            return "ERROR: CPF inválido! Deve conter exatamente 11 digítos numéricos";
        }

        int id= repository.proximoId();
        Aluno aluno = new Aluno(id, nome.trim(), cpf);
        repository.salvar(aluno);
        return "Aluno cadsatrado com sucesso(ID:" + id + ")";
    }

    public List<Aluno> listarTodos() {
        return repository.listarTodos();
    }

    public void deletarTodos(){
        repository.deletarTodos();
    }


}
