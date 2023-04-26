package tech.saturdev.pakakumi.util;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import tech.saturdev.pakakumi.models.Request;
import tech.saturdev.pakakumi.repository.RequestRepository;

@WebFilter("*")
public class RequestFilter implements Filter {

    private final RequestRepository requestRepository;

    public RequestFilter(RequestRepository repository) {
        this.requestRepository = repository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        if (!httpRequest.getRequestURI().equals("/requests")) {
            Request request = new Request();
            request.setRequestUrl(httpRequest.getRequestURI());
            request.setIpAddress(servletRequest.getRemoteAddr());
            request.setTime(LocalDateTime.now());
            request.setUserAgent(httpRequest.getHeader("User-Agent"));
            requestRepository.save(request);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
