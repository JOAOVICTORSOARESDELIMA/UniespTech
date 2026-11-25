package br.com.uniesptech.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.uniesptech.model.Aluno;
import br.com.uniesptech.repository.MemoriaAlunoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlunoServiceTest {

    private AlunoService service;

    @BeforeEach
    void setUp() {
        service = new AlunoService(new MemoriaAlunoRepository());
    }

    @Test
    void cadastraAlunoValido() {
        Aluno aluno = service.cadastrar("  Maria   Eduarda ", "529.982.247-25");
        assertEquals("Maria Eduarda", aluno.nome());
        assertEquals("52998224725", aluno.cpf());
        assertEquals(1, service.listar().size());
    }

    @Test
    void recusaNomeVazio() {
        assertThrows(ValidacaoException.class, () -> service.cadastrar("   ", "52998224725"));
        assertThrows(ValidacaoException.class, () -> service.cadastrar(null, "52998224725"));
    }

    @Test
    void recusaNomeLongoDemais() {
        String nome = "a".repeat(AlunoService.TAMANHO_MAXIMO_NOME + 1);
        assertThrows(ValidacaoException.class, () -> service.cadastrar(nome, "52998224725"));
    }

    @Test
    void recusaCpfInvalido() {
        ValidacaoException e = assertThrows(ValidacaoException.class,
                () -> service.cadastrar("João", "123"));
        assertTrue(e.getMessage().contains("11 dígitos"));
        assertThrows(ValidacaoException.class, () -> service.cadastrar("João", "52998224724"));
    }

    @Test
    void recusaCpfDuplicado() {
        service.cadastrar("Maria", "52998224725");
        assertThrows(ValidacaoException.class, () -> service.cadastrar("Outra Maria", "529.982.247-25"));
        assertEquals(1, service.listar().size());
    }

    @Test
    void deletarTodosDevolveQuantidade() {
        service.cadastrar("Maria", "52998224725");
        service.cadastrar("Lucas", "11144477735");
        assertEquals(2, service.deletarTodos());
        assertTrue(service.listar().isEmpty());
    }
}
