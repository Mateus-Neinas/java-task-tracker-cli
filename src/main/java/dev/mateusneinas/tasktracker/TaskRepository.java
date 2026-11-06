package dev.mateusneinas.tasktracker;

import java.util.List;

/** Porta de persistência: o serviço não conhece detalhes de arquivo ou banco. */
public interface TaskRepository {
    List<Task> findAll();
    void saveAll(List<Task> tasks);
}