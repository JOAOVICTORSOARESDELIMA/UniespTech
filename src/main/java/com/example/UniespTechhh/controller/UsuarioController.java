package com.example.UniespTechhh.controller;//para n se assustar veja o readme primeiro ksksk coloquei uma mensagem bem legal quando esse carrosel roda

import com.example.UniespTechhh.model.Usuario;
import com.example.UniespTechhh.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //Avisa pro Spring que aqui é a central de rotas da internet pq ne se ele num sabe oq e oq como vai direcionar o cadastro e os outros? ele n e a Raven n
@RequestMapping("/usuarios") // Tudo que começar com /usuarios vem parar aqui
public class UsuarioController {

    @Autowired
    private UsuarioService servico; // Chama o cara que faz o servico pesado "trabaia escravo trabaia"

    //Antiga opção 1 Cadastrar AEEEEE N QUEBREI O CODIGUUU
    //Agora vc pode spikar com o POST no http://localhost:8080/usuarios/cadastrar
    @PostMapping("/cadastrar")
    public String cadastrar(@Valid @RequestBody Usuario user) {
        //O @Valid ali em cima e o ajudante do anti b.o se o CPF tiver errado ele num deixa passa
        servico.salvar(user);
        return "Usuário " + user.getNome() + " salvo com sucesso! (Nível: " + user.getNivel() + ")";
    }

    //Antiga opcao 2 Listar all people ksksk
    //Manda spikar o GET no  http://localhost:8080/usuarios/listar e ve o "do ba ridu" rolar e aquele meme dos caras cantando pra o banco de dados voltar
    @GetMapping("/listar")
    public List<Usuario> listar() {
        return servico.listarTodos(); // Pega a lista de geral do banco e como diria smzinho "Agora sou eu e vc"
    }

    //(Thanos)Antiga opcao 3 Deletar tudo os avemarias aqui n te salvam se vc apagou errado então cuidado ou n dá uma de thanos e se ferro :)
    @DeleteMapping("/limpar-geral")
    public String deletarGeral() {
        servico.deletarTudo();
        return "Pronto, excluiu tudo... espero que fosse isso mesmo se não tres avemarias e cuidado pra o chefe n ver ksksk";
    }
}