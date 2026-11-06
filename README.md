# Java Task Tracker CLI

Aplicação Java 17 de linha de comando para gerenciar tarefas locais, sem dependências de infraestrutura. O projeto demonstra organização em camadas, persistência em arquivo, tratamento de erros e testes unitários.

## Funcionalidades

- Criar tarefas
- Listar tarefas pendentes antes das concluídas
- Concluir ou remover uma tarefa pelo id
- Persistir dados localmente em `data/tasks.tsv`
- Executar testes com JUnit 5

## Pré-requisitos e execução

É necessário Java 17+ e Maven 3.9+.

```bash
mvn test
mvn package
java -cp target/java-task-tracker-cli-1.0.0.jar dev.mateusneinas.tasktracker.Main add "Estudar Java"
java -cp target/java-task-tracker-cli-1.0.0.jar dev.mateusneinas.tasktracker.Main list
java -cp target/java-task-tracker-cli-1.0.0.jar dev.mateusneinas.tasktracker.Main done 1
```

## Arquitetura

```
Main (CLI) → TaskService (regras) → TaskRepository (porta) → FileTaskRepository (arquivo)
```

A interface `TaskRepository` evita acoplamento entre as regras do sistema e a tecnologia de persistência. Por isso, é fácil trocar o arquivo por SQLite, uma API ou banco relacional posteriormente.

## Guia rápido do código

| Classe | Responsabilidade |
| --- | --- |
| `Task` | Entidade imutável; valida id, título e data. `complete()` devolve uma cópia concluída. |
| `TaskService` | Regras de negócio: `add`, `list`, `complete` e `remove`. |
| `TaskRepository` | Contrato mínimo para carregar e gravar tarefas. |
| `FileTaskRepository` | Lê e grava TSV; codifica títulos em Base64 para preservar caracteres especiais. |
| `Main` | Interpreta argumentos do terminal e mostra o resultado ao usuário. |
| `TaskServiceTest` | Teste da criação e conclusão usando repositório em memória. |

## Próximos passos possíveis

- filtros por status
- prazo e prioridade
- exportação JSON
- interface gráfica ou API REST

## Licença

MIT.
