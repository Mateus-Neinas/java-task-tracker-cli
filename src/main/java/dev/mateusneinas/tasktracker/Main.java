package dev.mateusneinas.tasktracker;

import java.nio.file.Path;
import java.time.Clock;
import java.util.List;

/** Ponto de entrada e adaptador de linha de comando da aplicação. */
public final class Main {
    private Main() {}

    public static void main(String[] args) {
        TaskService service = new TaskService(new FileTaskRepository(Path.of("data", "tasks.tsv")), Clock.systemUTC());
        try {
            execute(service, args);
        } catch (IllegalArgumentException | TaskStorageException exception) {
            System.err.println("Erro: " + exception.getMessage());
            printUsage();
        }
    }

    static void execute(TaskService service, String[] args) {
        if (args.length == 0) { printUsage(); return; }
        switch (args[0]) {
            case "add" -> add(service, args);
            case "list" -> list(service, args);
            case "done" -> System.out.println("Concluída: " + service.complete(parseId(args)).title());
            case "reopen" -> System.out.println("Reaberta: " + service.reopen(parseId(args)).title());
            case "remove" -> { service.remove(parseId(args)); System.out.println("Tarefa removida."); }
            case "help" -> printUsage();
            default -> throw new IllegalArgumentException("Comando desconhecido: " + args[0]);
        }
    }

    private static void add(TaskService service, String[] args) {
        if (args.length < 2) throw new IllegalArgumentException("Informe o título da tarefa.");
        Task task = service.add(String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length)));
        System.out.println("Criada #" + task.id() + ": " + task.title());
    }

    private static void list(TaskService service, String[] args) {
        if (args.length > 2) throw new IllegalArgumentException("Uso: list [all|pending|done]");
        Boolean completed = null;
        if (args.length == 2) {
            completed = switch (args[1]) {
                case "all" -> null;
                case "pending" -> false;
                case "done" -> true;
                default -> throw new IllegalArgumentException("Filtro inválido: " + args[1]);
            };
        }
        list(service.list(completed));
    }

    private static void list(List<Task> tasks) {
        if (tasks.isEmpty()) { System.out.println("Nenhuma tarefa cadastrada."); return; }
        tasks.forEach(task -> System.out.printf("#%d [%s] %s%n", task.id(), task.completed() ? "x" : " ", task.title()));
    }

    private static long parseId(String[] args) {
        if (args.length != 2) throw new IllegalArgumentException("Informe somente o id da tarefa.");
        return Long.parseLong(args[1]);
    }

    private static void printUsage() {
        System.out.println("Uso: add <título> | list [all|pending|done] | done <id> | reopen <id> | remove <id> | help");
    }
}