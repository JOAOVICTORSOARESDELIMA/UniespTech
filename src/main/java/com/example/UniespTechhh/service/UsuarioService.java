package com.example.UniespTechhh.service;//para n se assustar veja o readme primeiro ksksk coloquei uma mensagem bem legal quando esse carrosel roda

import com.example.UniespTechhh.model.Usuario;
import com.example.UniespTechhh.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List; //Importante pra lista funcionar n esquecer de botar em outras coisas eu de dps

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repo; //O cara que manda e desmanda no banco de dados

    //Salva o meliante com as regrinhas que a gente fez
    public Usuario salvar(Usuario user) {
        //Se o cara n disse o que e a gente nem conversa n somos advinhos pra saber coisa sem ter informacao
        if (user.getNivel() == null) {
            throw new RuntimeException("Esqueceu o nível, foi? É Aluno ou Professor?");
        }

        //Logica que eu tava doida pra testar e um trem de níveis ado a ado cada um no seu quadrado
        if (user.getNivel().equalsIgnoreCase("ALUNO")) {
            user.setDisciplina(null); //Aluno não dá aula so estuda ou tenta que nem eu
        } else if (user.getNivel().equalsIgnoreCase("PROFESSOR")) {
            user.setMatricula(null); //Professor ja passou da fase de ter matricula de aluno
        }

        return repo.save(user); //Salva no banco e fé no pai que o dado sai
    }

    //Esse aqui é o listarTodos que o controller precisa pra mostrar oq tem ali dentro
    public List<Usuario> listarTodos() {
        return repo.findAll(); //Puxa as povas todas que tá salva na tabela
    }

    //O proprio thanos
    public void deletarTudo() {
        repo.deleteAll(); //Apaga geral então cuidado(Provavel que vou mudar )
    }
}