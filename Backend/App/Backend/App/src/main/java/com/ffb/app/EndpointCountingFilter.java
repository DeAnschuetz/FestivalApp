package com.ffb.app;


import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;

import jakarta.ws.rs.ext.Provider;

@Provider
public class EndpointCountingFilter {

    @Inject EndpointCounterStore store;

    @ServerRequestFilter
    public void req(ContainerRequestContext req) {
        // Do Notihng
    }

    @ServerResponseFilter
    public void resp(ContainerRequestContext req, ContainerResponseContext res) {
        String method = req.getMethod();
        String path = req.getUriInfo().getPath();
        if (!path.startsWith("/")) path = "/" + path;
        String normalized = path.replaceAll("/\\{[^/}]+}", "/{param}")
                .replaceAll("/[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}", "/{param}")
                .replaceAll("/(ORDERED)|/(IN_PROGRESS)|/(DONE)|/(READY_FOR_PICKUP)|/(CANCELED)", "/{param}")
                .replaceAll("/(NEW)|/(READ)|/(REMOVED)", "/{param}")
                .replaceAll("/(true)|/(false)", "/{param}")
        ;

        store.increment(method + " " + normalized);
    }
}