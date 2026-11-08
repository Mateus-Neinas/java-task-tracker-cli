# Java Task Tracker CLI

Aplicação Java 17 de linha de comando para gerenciar tarefas locais, sem dependências de infraestrutura. O projeto demonstra organização em camadas, persistência em arquivo, tratamento de erros e testes unitários.

## Funcionalidades

- Criar tarefas
- Listar todas, somente pendentes ou somente concluídas
- Concluir, reabrir ou remover uma tarefa pelo id
- Persistir dados localmente em `data/tasks.tsv`
- Executar testes com JUnit 5

## Pré-requisitos e execução

É necessário Java 17+ e Maven 3.9+.

```bash
mvn test
mvn package
java -cp target/java-task-tracker-cli-1.0.0.jar dev.mateusneinas.tasktracker.Main add "Estudar Java"
java -cp target/java-task-tracker-cli-1.0.0.jar dev.mateusneinas.tasktracker.Main list
java -cp target/java-task-tracker-cli-1.0.0.jar dev.mateusneinas.tasktracker.Main list pending
java -cp target/java-task-tracker-cli-1.0.0.jar dev.mateusneinas.tasktracker.Main list done
java -cp target/java-task-tracker-cli-1.0.0.jar dev.mateusneinas.tasktracker.Main done 1
java -cp target/java-task-tracker-cli-1.0.0.jar dev.mateusneinas.tasktracker.Main reopen 1
```

O filtro aceita `all`, `pending` ou `done`; sem filtro, `list` mostra todas as tarefas.

## Arquitetura

```
Main (CLI) → TaskService (regras) → TaskRepository (porta) → FileTaskRepository (arquivo)
```

A interface `TaskRepository` evita acoplamento entre as regras do sistema e a tecnologia de persistência. Por isso, é fácil trocar o arquivo por SQLite, uma API ou banco relacional posteriormente.

## Guia rápido do código

| Classe/método | Responsabilidade |
| --- | --- |
| `Task` | Entidade imutável; valida id, título e data. `complete()` e `reopen()` devolvem novas cópias com o status alterado. |
| `TaskService.add` | Cria tarefa com id crescente e horário fornecido pelo `Clock`. |
| `TaskService.list()` | Preserva a listagem completa e ordenada que já existia. |
| `TaskService.list(Boolean)` | Filtra por status; `null` retorna todas, `false` pendentes e `true` concluídas. |
| `TaskService.complete/reopen/remove` | Atualiza o status ou remove tarefa, validando o id existente. |
| `TaskRepository` / `FileTaskRepository` | Contrato de armazenamento e implementação TSV local. |
| `Main` | Interpreta argumentos; `list` valida o filtro e delega ao serviço. |
| `TaskServiceTest` | Cobre criação, conclusão, reabertura e os dois filtros de status. |

## Licença

MIT.
