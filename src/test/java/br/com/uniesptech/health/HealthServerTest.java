package br.com.uniesptech.health;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.uniesptech.repository.MemoriaAlunoRepository;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;

class HealthServerTest {

    @Test
    void respondeUpQuandoArmazenamentoOk() throws Exception {
        assertStatus(new MemoriaAlunoRepository(), 200, "\"status\":\"UP\"");
    }

    @Test
    void respondeDownQuandoArmazenamentoFalha() throws Exception {
        MemoriaAlunoRepository quebrado = new MemoriaAlunoRepository() {
            @Override
            public boolean estaDisponivel() {
                return false;
            }
        };
        assertStatus(quebrado, 503, "\"status\":\"DOWN\"");
    }

    private static void assertStatus(MemoriaAlunoRepository repo, int codigo, String trecho)
            throws Exception {
        HealthServer servidor = new HealthServer(0, repo); // porta 0 = qualquer porta livre
        servidor.iniciar();
        try {
            HttpClient cliente = HttpClient.newBuilder().proxy(HttpClient.Builder.NO_PROXY).build();
            HttpResponse<String> resposta = cliente.send(
                    HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + servidor.porta() + "/health")).build(),
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(codigo, resposta.statusCode());
            assertTrue(resposta.body().contains(trecho));
        } finally {
            servidor.parar();
        }
    }
}
