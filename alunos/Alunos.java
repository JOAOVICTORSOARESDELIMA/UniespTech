package alunos;
//explication pra no me perder Alunos o objeto tem os valores e seu construtor, o cosntrutor faz com que aqueles valores va pra o canto certin do objeto
//Aí "A pq tem get e set aqui ?" Tem pq a gente precisa fazer o objeto se comunicar com as outras coisas tipo o listar e um metodo

//@Table(name = "alunos") Adiantar neh e bom  que e ctrl c ctrl v quando colocar o banco
//@Entity(name = "alunos/aluno") vou ver os nomes dps


//aqui ja e o objeto sendo delimitado os valores
public class Alunos {
    private int id;
    private String nome;
    private String senha;
    private String gmail;
    private long matricula;
    private long cpf;
//construtor n esquece dnv pela mor de deus n adianta dizer esse objeto tem valor x mas se vc n fala como construir complica
    public Alunos(int id,String nome , String senha, String gmail, Long matricula, Long cpf ) {
        this.id =id;
        this.nome = nome;
        this.senha = senha;
        this.gmail = gmail;
        this.matricula = matricula;
        this.cpf = cpf;

    }
//pegar
    public int getid(){
    return id;
    }
    public String getNome(){
        return nome;
    }
    public String getSenha(){
        return senha;
    }
    public String getGmail(){
        return gmail;
    }
    public long getMatricula(){
        return matricula;
    }
    public long getCpf() {
        return cpf;
    }
//atualizar
    public int setid(){
    return id;
    }
    public String setNome(){
        return nome;
    }
    public String setSenha(){
        return senha;
    }
    public String setGmail(){
        return gmail;
    }
    public long setMatricula(){
        return matricula;
    }
    public long setCpf() {
        return cpf;
    }


}
