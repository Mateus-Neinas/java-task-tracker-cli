package dev.mateusneinas.tasktracker;

/** Exceção de domínio para falhas de leitura ou gravação da lista local. */
public final class TaskStorageException extends RuntimeException {
    public TaskStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}