package dev.mateusneinas.tasktracker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/** Implementação do repositório que persiste tarefas em um arquivo TSV simples. */
public final class FileTaskRepository implements TaskRepository {
    private final Path storage;

    public FileTaskRepository(Path storage) {
        this.storage = storage;
    }

    @Override
    public List<Task> findAll() {
        if (Files.notExists(storage)) return new ArrayList<>();
        try {
            List<Task> tasks = new ArrayList<>();
            for (String line : Files.readAllLines(storage, StandardCharsets.UTF_8)) {
                if (!line.isBlank()) tasks.add(decode(line));
            }
            return tasks;
        } catch (IOException e) {
            throw new TaskStorageException("Não foi possível ler " + storage, e);
        }
    }

    @Override
    public void saveAll(List<Task> tasks) {
        try {
            Path parent = storage.getParent();
            if (parent != null) Files.createDirectories(parent);
            Files.write(storage, tasks.stream().map(this::encode).toList(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new TaskStorageException("Não foi possível salvar " + storage, e);
        }
    }

    private String encode(Task task) {
        String title = Base64.getUrlEncoder().encodeToString(task.title().getBytes(StandardCharsets.UTF_8));
        return task.id() + "\t" + task.completed() + "\t" + task.createdAt() + "\t" + title;
    }

    private Task decode(String line) {
        String[] parts = line.split("\\t", 4);
        if (parts.length != 4) throw new TaskStorageException("Arquivo de tarefas corrompido.", null);
        String title = new String(Base64.getUrlDecoder().decode(parts[3]), StandardCharsets.UTF_8);
        return new Task(Long.parseLong(parts[0]), title, Boolean.parseBoolean(parts[1]), Instant.parse(parts[2]));
    }
}