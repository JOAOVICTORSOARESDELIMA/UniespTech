package com.example.UniespTechhh.repository;//para n se assustar veja o readme primeiro ksksk coloquei uma mensagem bem legal quando esse carrosel roda

import com.example.UniespTechhh.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//dps fazer anotações aqui pra mior explicação do pq ele e assim, n adianta sabe que e assim e ponto, tem que saber pq
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}