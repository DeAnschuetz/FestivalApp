package com.ffb.app;

import io.quarkus.runtime.StartupEvent;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.Route;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class EndpointRouteSeeder {

    @Inject Router router;
    @Inject EndpointCounterStore store;

    void onStart(@Observes StartupEvent ev) {
        for (Route r : router.getRoutes()) {
            if (r.getPath() == null) continue;
            if (r.getPath().startsWith("/q/")) continue;
            if (r.methods() == null || r.methods().isEmpty()) continue;


            r.methods().forEach(m ->
                    store.ensureKeyExists(m.name() + " " + normalize(r.getPath()))
            );

        }
    }

    private String normalize(String path) {
        return path
                .replaceAll("/\\d+(?=/|$)", "/{id}")
                .replaceAll("/[0-9a-fA-F-]{36}(?=/|$)", "/{uuid}");
    }
}