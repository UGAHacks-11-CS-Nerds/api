package com.agentdid127.notiscanapi.api;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CORSRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (requestContext.getRequest().getMethod().equals("OPTIONS")) {

            Response response =
                    Response.status(Response.Status.OK)
                            .header("Access-Control-Allow-Methods", "GET, POST, PUT")
                            .header("Access-Control-Allow-Origin", "*")
                            .header("Access-Control-Allow-Headers", "*")
                            .build();
            requestContext.abortWith(response);
        }
    }
}