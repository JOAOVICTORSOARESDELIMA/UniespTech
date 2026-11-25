package br.com.uniesptech.health;

import br.com.uniesptech.repository.AlunoRepository;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * Endpoint HTTP {@code GET /health} para monitoramento.
 *
 * <p>Responde 200 com {@code {"status":"UP"}} quando o armazenamento está
 * acessível e 503 com {@code {"status":"DOWN"}} quando não está. Usa o servidor
 * HTTP que já vem no JDK, sem dependências externas.</p>
 */
public class HealthServer {

    private static final Logger LOG = Logger.getLogger(HealthServer.class.getName());

    private final HttpServer servidor;
    private final AlunoRepository repository;

    public HealthServer(int porta, AlunoRepository repository) throws IOException {
        this.repository = repository;
        this.servidor = HttpServer.create(new InetSocketAddress(porta), 0);
        this.servidor.createContext("/health", this::responder);
    }

    public void iniciar() {
        servidor.start();
        LOG.info(() -> "Health check disponível em http://localhost:" + porta() + "/health");
    }

    public void parar() {
        servidor.stop(0);
    }

    public int porta() {
        return servidor.getAddress().getPort();
    }

    private void responder(HttpExchange troca) throws IOException {
        try (troca) {
            if (!"GET".equals(troca.getRequestMethod())) {
                troca.sendResponseHeaders(405, -1);
                return;
            }
            boolean ok = repository.estaDisponivel();
            if (!ok) {
                LOG.severe("Health check: armazenamento indisponível");
            }
            String corpo = "{\"status\":\"" + (ok ? "UP" : "DOWN") + "\",\"armazenamento\":\""
                    + (ok ? "OK" : "INDISPONIVEL") + "\"}";
            byte[] bytes = corpo.getBytes(StandardCharsets.UTF_8);
            troca.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            troca.sendResponseHeaders(ok ? 200 : 503, bytes.length);
            try (OutputStream out = troca.getResponseBody()) {
                out.write(bytes);
            }
        }
    }
}
