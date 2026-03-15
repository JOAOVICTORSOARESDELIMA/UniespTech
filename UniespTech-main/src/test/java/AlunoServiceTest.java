import Service.AlunoService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AlunoServiceTest {

    @Test
    void deveCadastrarAlunoComDadosValidos() {
        AlunoService service = new AlunoService();
        boolean resultado = service.cadastrarAluno("Lucas", "12345678901");
        assertTrue(resultado);
    }

    @Test
    void naoDeveCadastrarAlunoComNomeVazio() {
        AlunoService service = new AlunoService();
        boolean resultado = service.cadastrarAluno("", "12345678901");
        assertFalse(resultado);
    }

    @Test
    void naoDeveCadastrarAlunoComCpfInvalido() {
        AlunoService service = new AlunoService();
        boolean resultado = service.cadastrarAluno("Lucas", "123");
        assertFalse(resultado);
    }

    @Test
    void deveListarAlunoCadastrado() {
        AlunoService service = new AlunoService();
        service.cadastrarAluno("Lucas", "12345678901");
        assertEquals(1, service.listarAlunos().size());
    }
}