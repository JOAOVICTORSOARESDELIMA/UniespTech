package com.example.UniespTechhh;//para n se assustar veja o readme primeiro ksksk coloquei uma mensagem bem legal quando esse carrosel roda

import com.example.UniespTechhh.model.Usuario;
import com.example.UniespTechhh.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // O Abençoado testa esta bixiga do sistema riga ou deriga?
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService servico;

    @Test //Teste do cadastro da victoria screts  pq da victoria screts?? pq o cadastro vai ficar bonito se passar e feio se num for
    public void testarCadastroSucesso() {
        Usuario user = new Usuario();
        user.setNome("Smzin michaeljackson da silva");
        user.setCpf("12345678901");
        user.setNivel("ALUNO");

        Usuario salvo = servico.salvar(user);
        assertNotNull(salvo.getId()); //Se o banco deu um ID estouraremos o champanhe pq fununciou
    }

    @Test //Teste da logica do nivel eu tava doida pra testar issuuu
    public void testarProtecaoDeNivel() {
        Usuario prof = new Usuario();
        prof.setNivel("PROFESSOR");
        prof.setMatricula("SOU_ALUNO_SQN"); //Tentando botar matrícula em professor se quebrar aí nois chora

        Usuario salvo = servico.salvar(prof);
        assertNull(salvo.getMatricula()); //O sistema tem que ter limpado esse djenho se n a logica ta cagada
    }
}