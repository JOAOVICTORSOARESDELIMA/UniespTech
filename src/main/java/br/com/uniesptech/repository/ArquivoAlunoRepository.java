package br.com.uniesptech.repository;

import br.com.uniesptech.model.Aluno;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Repositório que grava os alunos num arquivo de texto (um aluno por linha:
 * {@code id;cpf;nome}). Os dados sobrevivem ao reinício da aplicação.
 *
 * <p>Cada gravação escreve um arquivo temporário e depois troca pelo
 * definitivo, para não corromper os dados se o programa cair no meio.</p>
 */
public class ArquivoAlunoRepository implements AlunoRepository {

    private static final Logger LOG = Logger.getLogger(ArquivoAlunoRepository.class.getName());
    private static final String SEPARADOR = ";";

    private final Path arquivo;
    private final List<Aluno> alunos = new ArrayList<>();
    private long proximoId = 1;

    public ArquivoAlunoRepository(Path arquivo) {
        this.arquivo = arquivo;
        carregar();
    }

    @Override
    public synchronized Aluno salvar(String nome, String cpf) {
        Aluno aluno = new Aluno(proximoId++, nome, cpf);
        alunos.add(aluno);
        try {
            gravar();
        } catch (UncheckedIOException e) {
            alunos.remove(aluno); // mantém a memória igual ao arquivo
            throw e;
        }
        return aluno;
    }

    @Override
    public synchronized List<Aluno> listarTodos() {
        return List.copyOf(alunos);
    }

    @Override
    public synchronized Optional<Aluno> buscarPorCpf(String cpf) {
        return alunos.stream().filter(a -> a.cpf().equals(cpf)).findFirst();
    }

    @Override
    public synchronized void deletarTodos() {
        List<Aluno> anteriores = List.copyOf(alunos);
        alunos.clear();
        try {
            gravar();
        } catch (UncheckedIOException e) {
            alunos.addAll(anteriores);
            throw e;
        }
    }

    @Override
    public boolean estaDisponivel() {
        Path pasta = arquivo.toAbsolutePath().getParent();
        return pasta != null && Files.isDirectory(pasta) && Files.isWritable(pasta)
                && (!Files.exists(arquivo) || Files.isReadable(arquivo));
    }

    private void carregar() {
        if (!Files.exists(arquivo)) {
            return;
        }
        try {
            for (String linha : Files.readAllLines(arquivo, StandardCharsets.UTF_8)) {
                if (linha.isBlank()) {
                    continue;
                }
                // O nome é o último campo: limit 3 preserva um ';' dentro do nome.
                String[] partes = linha.split(SEPARADOR, 3);
                long id = Long.parseLong(partes[0]);
                alunos.add(new Aluno(id, partes[2], partes[1]));
                proximoId = Math.max(proximoId, id + 1);
            }
            LOG.info(() -> "Dados carregados: " + alunos.size() + " aluno(s) de " + arquivo);
        } catch (IOException | RuntimeException e) {
            throw new IllegalStateException("Não foi possível ler o arquivo de dados " + arquivo, e);
        }
    }

    private void gravar() {
        try {
            Path pasta = arquivo.toAbsolutePath().getParent();
            Files.createDirectories(pasta);
            Path temporario = Files.createTempFile(pasta, "alunos", ".tmp");
            List<String> linhas = new ArrayList<>();
            for (Aluno a : alunos) {
                linhas.add(a.id() + SEPARADOR + a.cpf() + SEPARADOR + a.nome());
            }
            Files.write(temporario, linhas, StandardCharsets.UTF_8);
            Files.move(temporario, arquivo, StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            LOG.severe(() -> "Falha ao gravar " + arquivo + ": " + e.getMessage());
            throw new UncheckedIOException("Não foi possível salvar os dados", e);
        }
    }
}
