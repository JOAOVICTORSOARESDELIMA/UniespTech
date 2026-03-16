import service.AlunoService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AlunoServiceTest {

    @Test
    void deveCadastrarAlunoComDadosValidos() {
        AlunoService service = new AlunoService();
        boolean resultado = service.cadastrarAluno("Maria Eduarda", "98765432100");
        assertTrue(resultado);
    }

    @Test
    void naoDeveCadastrarAlunoComNomeVazio() {
        AlunoService service = new AlunoService();
        boolean resultado = service.cadastrarAluno("", "98765432100");
        assertFalse(resultado);
    }

    @Test
    void naoDeveCadastrarAlunoComCpfInvalido() {
        AlunoService service = new AlunoService();
        boolean resultado = service.cadastrarAluno("Maria Eduarda", "12345");
        assertFalse(resultado);
    }

    @Test
    void deveListarAlunoCadastrado() {
        AlunoService service = new AlunoService();
        service.cadastrarAluno("Maria Eduarda", "11122233344");
        assertEquals(1, service.listarAlunos().size());
    }
}