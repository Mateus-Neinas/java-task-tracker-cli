# Java Task Tracker CLI

Aplicação Java 17 de linha de comando para gerenciar tarefas locais, sem dependências de infraestrutura. O projeto demonstra organização em camadas, persistência em arquivo, tratamento de erros e testes unitários.

## Funcionalidades

- Criar tarefas
- Listar todas, somente pendentes ou somente concluídas
- Concluir, reabrir ou remover uma tarefa pelo id
- Exportar tarefas em JSON UTF-8
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
java -cp target/java-task-tracker-cli-1.0.0.jar dev.mateusneinas.tasktracker.Main export data/tasks.json
```

O filtro aceita `all`, `pending` ou `done`; sem filtro, `list` mostra todas as tarefas.

## Arquitetura

```
Main (CLI) → TaskService (regras) → TaskRepository (porta) → FileTaskRepository (TSV)
                   └──────────────→ TaskJsonExporter (JSON)
```

A interface `TaskRepository` evita acoplamento entre as regras do sistema e a tecnologia de persistência. A exportação JSON é uma saída independente; não altera o arquivo TSV original.

## Guia rápido do código

| Classe/método | Responsabilidade |
| --- | --- |
| `Task` | Entidade imutável; valida id, título e data. `complete()` e `reopen()` devolvem novas cópias com o status alterado. |
| `TaskService.add` | Cria tarefa com id crescente e horário fornecido pelo `Clock`. |
| `TaskService.list()` / `list(Boolean)` | Lista todas as tarefas ou filtra pendentes/concluídas. |
| `TaskService.complete/reopen/remove` | Atualiza o status ou remove tarefa, validando o id existente. |
| `TaskJsonExporter.export` | Cria diretórios necessários e grava a lista em UTF-8 no destino solicitado. |
| `TaskJsonExporter.toJson/escape` | Serializa os campos e escapa aspas, barras invertidas e caracteres de controle. |
| `Main.export` | Valida o argumento de caminho e solicita a exportação das tarefas. |
| `TaskRepository` / `FileTaskRepository` | Contrato de armazenamento e implementação TSV local. |
| `TaskServiceTest` / `TaskJsonExporterTest` | Cobrem regras de tarefa, filtros e serialização JSON. |

## Licença

MIT.
