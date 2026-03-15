package com.example.UniespTechhh.model;//para n se assustar veja o readme primeiro ksksk coloquei uma mensagem bem legal quando esse carrosel roda

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data //Cria o get e sett sem gastar dedo. Nosso amigo pros preguiçosos: lombok!
@Entity //Diz pro banco que isso e uma tabela/entidade
public class Usuario { //Nossa linda classe

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Gera o ID sozinho pra n ter que ficar 0, 1, 2 e blablablablba facilitador
    private Long id;

    @NotBlank(message = "Nome vazio não rola!") //Validando pra n vir campo em branco pq ninguem e sem terra e sem registro, n quero dor de cabeça
    private String nome;

    @Size(min = 11, max = 11, message = "CPF tem que ter 11 números,  é gorpe?!") //Regra do CPF e n ele n aceita cpf em branco a não ser que vc seja um sem regristro e sem terra ai "its not my problema"
    private String cpf;

    private String email;
    private String senha;

    //Nível de acesso criado por preguiça ou eficacia ? veja nos proximos capitulos se eu irei endoidar ou melhorar esse codigo
    private String nivel;

    //A pq c fez isso?? num quero ter que criar mais entidades ,_, agora e rezar que de certo e ir aprimorandoksksk
    private String matricula;


    private String disciplina;
}