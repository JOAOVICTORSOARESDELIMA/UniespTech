package br.com.uniesp.gestao.service;

import br.com.uniesp.gestao.model.Aluno;
import br.com.uniesp.gestao.repository.AlunoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AlunoService {

    private final AlunoRepository repository;

    public AlunoService(AlunoRepository repository) {
        this.repository = repository;
    }

    public void cadastrarAluno(Aluno aluno) {
        if (aluno.getNome() == null || aluno.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio!");
        }
        if (aluno.getCpf() == null || aluno.getCpf().length() != 11) {
            throw new IllegalArgumentException("CPF Inválido! Deve ter 11 dígitos.");
        }
        repository.save(aluno);
    }

    public List<Aluno> listarAlunos() {
        return repository.findAll();
    }

    public void limparBanco() {
        repository.deleteAll();
    }
}