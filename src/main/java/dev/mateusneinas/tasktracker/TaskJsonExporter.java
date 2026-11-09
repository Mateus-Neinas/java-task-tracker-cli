package dev.mateusneinas.tasktracker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Exporta a lista de tarefas em JSON UTF-8 sem dependências externas. */
public final class TaskJsonExporter {
    public void export(List<Task> tasks, Path destination) {
        String json = toJson(tasks);
        try {
            Path parent = destination.toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);
            Files.writeString(destination, json + System.lineSeparator(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new TaskStorageException("Não foi possível exportar para " + destination, exception);
        }
    }

    String toJson(List<Task> tasks) {
        StringBuilder json = new StringBuilder("[");
        for (int index = 0; index < tasks.size(); index++) {
            Task task = tasks.get(index);
            if (index > 0) json.append(',');
            json.append(System.lineSeparator()).append("  {")
                    .append("\"id\": ").append(task.id()).append(", ")
                    .append("\"title\": \"").append(escape(task.title())).append("\", ")
                    .append("\"completed\": ").append(task.completed()).append(", ")
                    .append("\"createdAt\": \"").append(task.createdAt()).append("\"}");
        }
        if (!tasks.isEmpty()) json.append(System.lineSeparator());
        return json.append(']').toString();
    }

    private String escape(String value) {
        StringBuilder escaped = new StringBuilder();
        for (char character : value.toCharArray()) {
            switch (character) {
                case '"' -> escaped.append("\\\"");
                case '\\' -> escaped.append("\\\\");
                case '\n' -> escaped.append("\\n");
                case '\r' -> escaped.append("\\r");
                case '\t' -> escaped.append("\\t");
                default -> {
                    if (character < 0x20) escaped.append(String.format("\\u%04x", (int) character));
                    else escaped.append(character);
                }
            }
        }
        return escaped.toString();
    }
}