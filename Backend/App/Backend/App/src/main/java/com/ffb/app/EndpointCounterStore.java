package com.ffb.app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

@ApplicationScoped
public class EndpointCounterStore {

    @Inject
    ObjectMapper mapper;

    @ConfigProperty(name = "app.endpoint-counter.file", defaultValue = "endpoint-counts.json")
    String fileName;

    private final ConcurrentHashMap<String, LongAdder> counters = new ConcurrentHashMap<>();

    private volatile boolean dirty = false;

    public void increment(String key) {
        counters.computeIfAbsent(key, k -> new LongAdder()).increment();
        dirty = true;
    }

    public void ensureKeyExists(String key) {
        counters.computeIfAbsent(key, k -> new LongAdder());
        dirty = true;
    }

    void onStart(@Observes StartupEvent ev) {
        loadFromFile();
    }

    void onStop(@Observes ShutdownEvent ev) {
        flushToFile(); // persist on shutdown
    }

    @Scheduled(every = "{app.endpoint-counter.flush-seconds}", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    void scheduledFlush() {
        flushToFile();
    }

    private Path path() {
        return Paths.get(fileName).toAbsolutePath();
    }

    private synchronized void loadFromFile() {
        Path p = path();
        if (!Files.exists(p)) return;

        try {
            byte[] json = Files.readAllBytes(p);
            Map<String, Long> snapshot = mapper.readValue(json, new TypeReference<>() {});
            snapshot.forEach((k, v) -> {
                LongAdder adder = new LongAdder();
                adder.add(v);
                counters.put(k, adder);
            });
        } catch (Exception e) {
            System.err.println("Failed to load endpoint counter file " + p + ": " + e.getMessage());
        }
    }

    private synchronized void flushToFile() {
        if (!dirty) return;

        Path p = path();
        try {
            Map<String, Long> snapshot = new java.util.TreeMap<>();
            counters.forEach((k, adder) -> snapshot.put(k, adder.sum()));

            Path tmp = p.resolveSibling(p.getFileName().toString() + ".tmp");
            byte[] json = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(snapshot);
            Files.write(tmp, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Files.move(tmp, p, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);

            dirty = false;
        } catch (Exception e) {
            System.err.println("Failed to flush endpoint counter file " + p + ": " + e.getMessage());
        }
    }
}