package dev.mateusneinas.tasktracker;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class TaskJsonExporterTest {
    @Test
    void exportsValidJsonAndEscapesSpecialCharacters() {
        Task task = new Task(7, "Aspas \" e barra \\ com\nlinha", true, Instant.parse("2026-01-01T00:00:00Z"));

        String json = new TaskJsonExporter().toJson(List.of(task));

        assertTrue(json.contains("\"id\": 7"));
        assertTrue(json.contains("\"title\": \"Aspas \\\" e barra \\\\ com\\nlinha\""));
        assertTrue(json.contains("\"completed\": true"));
        assertTrue(json.contains("\"createdAt\": \"2026-01-01T00:00:00Z\""));
    }

    @Test
    void exportsEmptyList() {
        assertTrue(new TaskJsonExporter().toJson(List.of()).equals("[]"));
    }
}