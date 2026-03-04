package com.ffb.app;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.ws.rs.*;
import io.quarkus.runtime.StartupEvent;

import java.lang.reflect.Method;
import java.util.Set;

@ApplicationScoped
public class JaxRsEndpointSeeder {

    private static final Set<Class<?>> RESOURCES = Set.of(
            com.ffb.app.api.AccountEndpointImpl.class,
            com.ffb.app.api.CartEndpointImpl.class,
            com.ffb.app.api.CreditEndpointImpl.class,
            com.ffb.app.api.FoodCourtEndpointImpl.class,
            com.ffb.app.api.FoodOrderEndpointImpl.class,
            com.ffb.app.api.MasterEndpointImpl.class,
            com.ffb.app.api.NotificationEndpointImpl.class,
            com.ffb.app.api.ProductAssignmentApi.class,
            com.ffb.app.api.ProductEndpointImpl.class,
            com.ffb.app.api.TicketEndpointImpl.class
    );

    void onStart(@Observes StartupEvent ev, EndpointCounterStore store) {
        for (Class<?> rc : RESOURCES) {
            Path classPathAnn = rc.getAnnotation(Path.class);
            String classPath = classPathAnn != null ? classPathAnn.value() : "";

            for (Method m : rc.getDeclaredMethods()) {
                String http = httpMethod(m);
                if (http == null) continue;

                Path methodPathAnn = m.getAnnotation(Path.class);
                String methodPath = methodPathAnn != null ? methodPathAnn.value() : "";

                String full = normalizePath(classPath, methodPath);
                store.ensureKeyExists(http + " " + full);
            }
        }
    }

    private static String httpMethod(Method m) {
        if (m.isAnnotationPresent(GET.class)) return "GET";
        if (m.isAnnotationPresent(POST.class)) return "POST";
        if (m.isAnnotationPresent(PUT.class)) return "PUT";
        if (m.isAnnotationPresent(DELETE.class)) return "DELETE";
        if (m.isAnnotationPresent(PATCH.class)) return "PATCH";
        if (m.isAnnotationPresent(HEAD.class)) return "HEAD";
        if (m.isAnnotationPresent(OPTIONS.class)) return "OPTIONS";
        return null;
    }

    private static String normalizePath(String a, String b) {
        String p = (a == null ? "" : a.trim()) + "/" + (b == null ? "" : b.trim());
        p = p.replaceAll("//+", "/");
        if (!p.startsWith("/")) p = "/" + p;
        if (p.length() > 1 && p.endsWith("/")) p = p.substring(0, p.length() - 1);

        p = p.replaceAll("/\\{[^/}]+}", "/{param}")
                .replaceAll("/[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}", "/{param}")
                .replaceAll("/(ORDERED)|/(IN_PROGRESS)|/(DONE)|/(READY_FOR_PICKUP)|/(CANCELED)", "/{param}")
                .replaceAll("/(NEW)|/(READ)|/(REMOVED)", "/{param}")
                .replaceAll("/(true)|/(false)", "/{param}")
        ;
        return p;
    }
}