package br.com.uniesptech.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.uniesptech.model.Aluno;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class ArquivoAlunoRepositoryTest {

    @Test
    void dadosSobrevivemAoReinicio() throws Exception {
        Path pasta = Files.createTempDirectory("uniesp");
        Path arquivo = pasta.resolve("sub/alunos.csv");

        ArquivoAlunoRepository primeiro = new ArquivoAlunoRepository(arquivo);
        primeiro.salvar("Maria; Eduarda", "52998224725");
        primeiro.salvar("Lucas", "11144477735");

        // "Reinicia" a aplicação criando outro repositório no mesmo arquivo.
        ArquivoAlunoRepository segundo = new ArquivoAlunoRepository(arquivo);
        List<Aluno> alunos = segundo.listarTodos();
        assertEquals(2, alunos.size());
        assertEquals("Maria; Eduarda", alunos.get(0).nome());
        assertTrue(segundo.buscarPorCpf("11144477735").isPresent());

        // O próximo id continua a sequência.
        assertEquals(3L, segundo.salvar("Ana", "39053344705").id());
    }

    @Test
    void deletarTodosLimpaOArquivo() throws Exception {
        Path arquivo = Files.createTempDirectory("uniesp").resolve("alunos.csv");
        ArquivoAlunoRepository repo = new ArquivoAlunoRepository(arquivo);
        repo.salvar("Maria", "52998224725");
        repo.deletarTodos();
        assertTrue(new ArquivoAlunoRepository(arquivo).listarTodos().isEmpty());
    }

    @Test
    void ficaDisponivelQuandoPastaExiste() throws Exception {
        Path arquivo = Files.createTempDirectory("uniesp").resolve("alunos.csv");
        assertTrue(new ArquivoAlunoRepository(arquivo).estaDisponivel());
    }
}
