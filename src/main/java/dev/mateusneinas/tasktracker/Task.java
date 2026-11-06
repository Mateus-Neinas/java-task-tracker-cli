package dev.mateusneinas.tasktracker;

import java.time.Instant;
import java.util.Objects;

/** Entidade imutável que representa uma tarefa registrada localmente. */
public record Task(long id, String title, boolean completed, Instant createdAt) {
    public Task {
        if (id < 1) throw new IllegalArgumentException("O id deve ser positivo.");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("O título é obrigatório.");
        title = title.trim();
        Objects.requireNonNull(createdAt, "A data de criação é obrigatória.");
    }

    /** Retorna uma cópia desta tarefa marcada como concluída. */
    public Task complete() {
        return new Task(id, title, true, createdAt);
    }
}