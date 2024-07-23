package com.sorinbratosin.licenta.Security;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;


@Component
public class ApiKeyFilter implements Filter {

    private static final String API_KEY_HEADER = "DeviceIOT";

    @Value("${api.key}")
    private String validApiKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String apiKey = httpRequest.getHeader("DeviceIOT");

        // Continua fara verificarea API key pt login, register si date-home endpoints
        if (httpRequest.getRequestURI().contains("/api/login") ||
                httpRequest.getRequestURI().contains("/api/register") ||
                httpRequest.getRequestURI().contains("/api/date-home")) {
            chain.doFilter(request, response);
            return;
        }

        if (validApiKey.equals(apiKey)) {
            grantDeviceAccess(httpRequest);
            chain.doFilter(request, response);
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Invalid API Key");
        }
    }

    private void grantDeviceAccess(HttpServletRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken
                ("device", null, Collections.singletonList(new SimpleGrantedAuthority("DEVICE")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
