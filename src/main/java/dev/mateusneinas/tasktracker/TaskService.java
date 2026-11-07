package dev.mateusneinas.tasktracker;

import java.time.Clock;
import java.util.Comparator;
import java.util.List;

/** Centraliza as regras de criação, conclusão, reabertura, remoção e ordenação de tarefas. */
public final class TaskService {
    private final TaskRepository repository;
    private final Clock clock;

    public TaskService(TaskRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    public Task add(String title) {
        List<Task> tasks = repository.findAll();
        long nextId = tasks.stream().mapToLong(Task::id).max().orElse(0) + 1;
        Task task = new Task(nextId, title, false, clock.instant());
        tasks.add(task);
        repository.saveAll(tasks);
        return task;
    }

    public List<Task> list() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Task::completed).thenComparing(Task::id))
                .toList();
    }

    public Task complete(long id) {
        List<Task> tasks = repository.findAll();
        Task task = findById(tasks, id);
        List<Task> updated = tasks.stream().map(item -> item.id() == id ? item.complete() : item).toList();
        repository.saveAll(updated);
        return task.complete();
    }

    public Task reopen(long id) {
        List<Task> tasks = repository.findAll();
        Task task = findById(tasks, id);
        List<Task> updated = tasks.stream().map(item -> item.id() == id ? item.reopen() : item).toList();
        repository.saveAll(updated);
        return task.reopen();
    }

    public void remove(long id) {
        List<Task> tasks = repository.findAll();
        findById(tasks, id);
        repository.saveAll(tasks.stream().filter(task -> task.id() != id).toList());
    }

    private Task findById(List<Task> tasks, long id) {
        return tasks.stream().filter(task -> task.id() == id).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada: " + id));
    }
}