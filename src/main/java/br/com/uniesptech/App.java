package br.com.uniesptech;

import br.com.uniesptech.controller.MenuController;
import br.com.uniesptech.health.HealthServer;
import br.com.uniesptech.repository.AlunoRepository;
import br.com.uniesptech.repository.ArquivoAlunoRepository;
import br.com.uniesptech.service.AlunoService;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Ponto de entrada. Configuração por variáveis de ambiente:
 * <ul>
 *   <li>{@code DATA_FILE}: arquivo de dados (padrão {@code dados/alunos.csv})</li>
 *   <li>{@code HEALTH_PORT}: porta do health check (padrão 8080; 0 desliga)</li>
 * </ul>
 */
public final class App {

    private App() {
    }

    public static void main(String[] args) throws Exception {
        // Logs numa linha só: "[NIVEL] data hora classe - mensagem"
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%4$s] %1$tF %1$tT %3$s - %5$s%6$s%n");
        Path arquivo = Path.of(env("DATA_FILE", "dados/alunos.csv"));
        int portaHealth = Integer.parseInt(env("HEALTH_PORT", "8080"));

        AlunoRepository repository = new ArquivoAlunoRepository(arquivo);
        AlunoService service = new AlunoService(repository);

        HealthServer health = null;
        if (portaHealth > 0) {
            health = new HealthServer(portaHealth, repository);
            health.iniciar();
        }
        try {
            boolean saiuPeloMenu =
                    new MenuController(service, new Scanner(System.in, "UTF-8"), System.out).executar();
            if (!saiuPeloMenu && health != null) {
                // Sem terminal (ex.: container em segundo plano): mantém o health check no ar.
                System.out.println("\nSem entrada no terminal. Health check continua ativo.");
                Thread.currentThread().join();
            }
        } finally {
            if (health != null) {
                health.parar();
            }
        }
    }

    private static String env(String nome, String padrao) {
        String valor = System.getenv(nome);
        return valor == null || valor.isBlank() ? padrao : valor;
    }
}
