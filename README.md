[![CI](https://github.com/JOAOVICTORSOARESDELIMA/UniespTech/actions/workflows/ci.yml/badge.svg)](https://github.com/JOAOVICTORSOARESDELIMA/UniespTech/actions/workflows/ci.yml)

# Uniesp Tech: sistema de gestão acadêmica

Desafio da disciplina de **DevOps** (Sistemas para Internet, UNIESP): pegar um sistema "macarrônico" herdado de uma startup falida ([`legado/SistemaUniesp.java`](legado/SistemaUniesp.java)) e transformá-lo numa aplicação organizada, testada, persistente, containerizada e com integração contínua.

## Antes e depois

| Problema do sistema legado | Como foi resolvido |
|---|---|
| Tudo num único `main` | Código separado em camadas: **Model, Repository, Service e Controller** |
| Dados só em memória (perde tudo ao reiniciar) | Persistência em **arquivo**, com gravação segura (arquivo temporário + troca atômica) |
| CPF validado só pelo tamanho | Validação real pelos **dígitos verificadores**, aceitando com ou sem pontuação, e bloqueio de **CPF duplicado** |
| "Deletar tudo" sem confirmação | Pede para digitar **APAGAR** antes de apagar |
| Nenhum teste | **16 testes JUnit 5** (validação, regras de negócio, persistência e health check) |
| Deploy manual por FTP | **Dockerfile multi-stage** e pipeline no **GitHub Actions** que gera o `.jar` como artefato |
| Ninguém sabe se o sistema está no ar | Endpoint **`GET /health`** e **logs** de cadastros, recusas e erros |

## Arquitetura

```
src/main/java/br/com/uniesptech/
├── App.java                       # inicia tudo e lê as configurações
├── model/Aluno.java               # dados do aluno (record)
├── repository/
│   ├── AlunoRepository.java       # interface: o serviço não sabe onde os dados ficam
│   ├── ArquivoAlunoRepository.java  # grava em arquivo (padrão)
│   └── MemoriaAlunoRepository.java  # em memória (usado nos testes)
├── service/
│   ├── AlunoService.java          # regras de negócio
│   ├── ValidadorCpf.java          # algoritmo dos dígitos verificadores
│   └── ValidacaoException.java
├── controller/MenuController.java  # menu do terminal
└── health/HealthServer.java       # GET /health
```

O projeto não tem dependências além do JDK (só o JUnit, nos testes). O health check usa o servidor HTTP que já vem no Java.

## Como executar

Requisitos: **Java 17** e **Maven**.

```bash
mvn package                          # compila, roda os testes e gera o .jar
java -jar target/sistema-academico.jar
```

Configuração por variáveis de ambiente:

| Variável | Padrão | Para que serve |
|---|---|---|
| `DATA_FILE` | `dados/alunos.csv` | Arquivo onde os alunos são gravados |
| `HEALTH_PORT` | `8080` | Porta do health check (`0` desliga) |

### Com Docker

```bash
docker build -t sistema-academico .
docker run -it -p 8080:8080 -v uniesp-dados:/app/dados sistema-academico
```

O volume `uniesp-dados` mantém os alunos mesmo se o container for recriado. Rodando em segundo plano (`-d`), o menu não aparece, mas o health check continua respondendo.

### Health check

```bash
curl http://localhost:8080/health
# {"status":"UP","armazenamento":"OK"}
```

Se a pasta de dados ficar inacessível, responde **503** com `"status":"DOWN"` e registra um log `SEVERE`. O `HEALTHCHECK` do Dockerfile usa esse endpoint.

## Testes

```bash
mvn test
```

| Classe de teste | O que verifica |
|---|---|
| `ValidadorCpfTest` | CPFs válidos e inválidos, pontuação, dígitos repetidos |
| `AlunoServiceTest` | Nome vazio ou longo, CPF inválido ou duplicado, apagar tudo |
| `ArquivoAlunoRepositoryTest` | Dados sobrevivem ao "reinício", ids continuam a sequência |
| `HealthServerTest` | `/health` responde 200 (UP) e 503 (DOWN) |

## Integração contínua

O workflow [`.github/workflows/ci.yml`](.github/workflows/ci.yml) roda a cada push e pull request na `main`:

1. compila e roda os testes com Maven;
2. publica o `sistema-academico.jar` como artefato do build;
3. constrói a imagem Docker.

## O que ficou de fora do enunciado

- **Banco de dados:** o enunciado sugere H2 ou PostgreSQL. Aqui a persistência é em arquivo, para o projeto rodar sem dependências. Como o acesso aos dados fica atrás da interface `AlunoRepository`, dá para trocar por um repositório JDBC sem mexer no resto do código.
- **Deploy contínuo em nuvem** (Render, Railway ou Fly.io) e **quadro Kanban** não fazem parte deste repositório.

---

<details>
<summary><b>Enunciado original do desafio</b></summary>

## Situação-Problema
A **Uniesp Tech** herdou um sistema de uma startup que faliu. O código atual é funcional, porém extremamente **frágil**:
* **Dados Voláteis:** Armazena dados apenas em memória (perde tudo ao reiniciar).
* **Sem Qualidade:** Não possui nenhum teste unitário ou de integração.
* **Deploy Artesanal:** O processo é manual (copiar o `.jar` via FTP).
* **Blindness (Cegueira):** Ninguém sabe se o sistema está online ou offline até que um usuário reclame.

---

## O Objetivo
Em **3 semanas**, vocês devem reconstruir a base deste sistema, aplicar persistência real, containerizar a aplicação e criar um fluxo de CI/CD profissional que impeça bugs de chegarem em produção.

---

## Cronograma de Desenvolvimento

### Semana 1: Refatoração, Qualidade e Governança (Plan, Code, Test)
Nesta fase, o foco é "organizar a casa" e garantir que o código seja testável e modular.

* **Desafio Java:**
    * Refatorar o código "macarrônico" original separando-o em camadas: `Controller`, `Service`, `Model` e `Repository`.
    * Implementar validações rigorosas (ex: CPF com 11 dígitos, campos obrigatórios não vazios).
* **Desafio DevOps:**
    * **Maven:** Configurar o `pom.xml` para gerenciar dependências e o ciclo de vida do projeto.
    * **Testes:** Criar os primeiros Testes Unitários com **JUnit 5** para as regras de negócio.
    * **Governança:** Criar um quadro Kanban (GitHub Projects). Proibido o Push direto na `main`; o código só entra via **Pull Request** com Code Review de um colega.

** Entrega:** Repositório organizado, código limpo e suite de testes rodando localmente.

---

### Semana 2: Persistência e Containerização (Build, Release)
Agora o sistema precisa sobreviver ao reinício e ser portável para qualquer ambiente.

* **Desafio Java:**
    * Substituir o armazenamento em `ArrayList` por persistência real.
    * Implementar **H2 Database** (em arquivo) ou **PostgreSQL** via JDBC ou Spring Data JPA.
* **Desafio DevOps:**
    * **Docker:** Criar um `Dockerfile` otimizado para a aplicação (Multi-stage build).
    * **CI (Continuous Integration):** Configurar **GitHub Actions** para que, a cada Push/PR, o sistema execute o Build e os Testes automaticamente.
    * **Artifacts:** O build bem-sucedido deve gerar uma imagem Docker ou um artefato `.jar` versionado no GitHub.

** Entrega:** Pipeline de CI configurado (build verde) e aplicação rodando dentro de um container Docker.

---

### Semana 3: Cloud, Deploy e Monitoramento (Deploy, Operate, Monitor)
Hora de colocar o sistema no mundo real e garantir que ele continue de pé e saudável.

* **Desafio Java:**
    * Implementar um endpoint de **Health Check** (ex: `/health`) que verifica a saúde do app e da conexão com o banco.
    * Implementar logs estruturados (Log4j ou SLF4J) para monitorar tentativas de cadastro ou erros críticos.
* **Desafio DevOps:**
    * **CD (Continuous Deployment):** Configurar o deploy automático para uma nuvem (Render, Railway ou Fly.io).
    * **Operação:** Realizar um "Chaos Test" (simular queda do banco e observar como a aplicação loga o erro).
    * **Documentação:** Finalizar o README com instruções de uso e evidências do monitoramento.

** Entrega:** Link da aplicação rodando em produção com fluxo de entrega contínua ativo.

---

## Semana 4: Prova Prática
Aplicação de uma *Hotfix de Emergência* em tempo real para avaliar o domínio sobre o fluxo DevOps construído.

</details>
