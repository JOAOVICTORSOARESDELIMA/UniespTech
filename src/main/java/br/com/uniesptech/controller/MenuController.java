package br.com.uniesptech.controller;

import br.com.uniesptech.model.Aluno;
import br.com.uniesptech.service.AlunoService;
import br.com.uniesptech.service.ValidacaoException;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/** Menu do terminal. Só cuida de ler e mostrar; as regras ficam no {@link AlunoService}. */
public class MenuController {

    private final AlunoService service;
    private final Scanner entrada;
    private final PrintStream saida;

    public MenuController(AlunoService service, Scanner entrada, PrintStream saida) {
        this.service = service;
        this.entrada = entrada;
        this.saida = saida;
    }

    /**
     * Roda o menu até o usuário escolher sair.
     *
     * @return {@code true} se o usuário saiu pelo menu; {@code false} se a entrada acabou
     *         (stdin fechado, como num container rodando em segundo plano)
     */
    public boolean executar() {
        while (true) {
            mostrarMenu();
            String opcao = lerLinha();
            if (opcao == null) {
                return false;
            }
            switch (opcao.trim()) {
                case "1" -> cadastrar();
                case "2" -> listar();
                case "3" -> deletarTudo();
                case "4" -> {
                    saida.println("Encerrando sistema...");
                    return true;
                }
                default -> saida.println("Opção inválida!");
            }
            saida.println();
        }
    }

    private void mostrarMenu() {
        saida.println("======= SISTEMA ACADÊMICO UNIESP TECH =======");
        saida.println("1 - Cadastrar aluno");
        saida.println("2 - Listar alunos");
        saida.println("3 - Apagar todos os alunos");
        saida.println("4 - Sair");
        saida.print("Escolha: ");
    }

    private void cadastrar() {
        saida.print("Nome do aluno: ");
        String nome = lerLinha();
        saida.print("CPF: ");
        String cpf = lerLinha();
        try {
            Aluno aluno = service.cadastrar(nome, cpf);
            saida.println("Aluno cadastrado com sucesso! ID: " + aluno.id());
        } catch (ValidacaoException e) {
            saida.println("ERRO: " + e.getMessage());
        }
    }

    private void listar() {
        List<Aluno> alunos = service.listar();
        saida.println("--- LISTA DE ALUNOS ---");
        if (alunos.isEmpty()) {
            saida.println("Nenhum aluno cadastrado.");
            return;
        }
        for (Aluno a : alunos) {
            saida.println("ID: " + a.id() + " | Nome: " + a.nome() + " | CPF: " + a.cpfFormatado());
        }
    }

    private void deletarTudo() {
        // Corrige o bug do sistema legado: agora pede confirmação antes de apagar.
        saida.print("Tem certeza que deseja apagar TODOS os alunos? Digite APAGAR para confirmar: ");
        String confirmacao = lerLinha();
        if ("APAGAR".equals(confirmacao == null ? null : confirmacao.trim())) {
            int quantidade = service.deletarTodos();
            saida.println(quantidade + " aluno(s) apagado(s).");
        } else {
            saida.println("Operação cancelada. Nada foi apagado.");
        }
    }

    private String lerLinha() {
        return entrada.hasNextLine() ? entrada.nextLine() : null;
    }
}
