package com.mubeen.mubeenmart.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Filter generating unique request ID for each incoming request and attaching to SLF4J MDC.
 * Conforms to Section 18 requirement:
 * "Structured logging via Logback: request ID generated per incoming request in a Filter, attached to the SLF4J MDC."
 */
@WebFilter("/*")
public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private static final String MDC_REQUEST_ID_KEY = "requestId";

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Generate clean short UUID for request tracking
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put(MDC_REQUEST_ID_KEY, requestId);
        httpResponse.setHeader("X-Request-ID", requestId);

        long startTime = System.currentTimeMillis();
        String uri = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        try {
            logger.debug("Incoming HTTP request: {} {}", method, uri);
            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = httpResponse.getStatus();
            logger.info("Completed HTTP request: {} {} -> status {} ({} ms)", method, uri, status, duration);
            MDC.remove(MDC_REQUEST_ID_KEY);
        }
    }

    @Override
    public void destroy() {
    }
}
