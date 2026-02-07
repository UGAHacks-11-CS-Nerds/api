package com.agentdid127.notiscanapi.api;


import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CORSResponseFilter implements ContainerResponseFilter {
    public void filter(
            ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        if (requestContext.getRequest().getMethod().equals("OPTIONS")) return;
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "*");
        headers.add("Access-Control-Allow-Headers", "*");
    }
}