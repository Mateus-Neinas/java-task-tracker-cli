package dev.mateusneinas.tasktracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class TaskServiceTest {
    @Test
    void createsCompletesAndReopensTask() {
        InMemoryRepository repository = new InMemoryRepository();
        TaskService service = new TaskService(repository, Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC));

        Task created = service.add("Estudar Java");

        assertEquals(1, created.id());
        assertEquals(1, service.list().size());
        assertTrue(service.complete(created.id()).completed());
        assertFalse(service.reopen(created.id()).completed());
        assertFalse(service.list().getFirst().completed());
    }

    private static final class InMemoryRepository implements TaskRepository {
        private List<Task> tasks = new ArrayList<>();
        public List<Task> findAll() { return new ArrayList<>(tasks); }
        public void saveAll(List<Task> tasks) { this.tasks = new ArrayList<>(tasks); }
    }
}