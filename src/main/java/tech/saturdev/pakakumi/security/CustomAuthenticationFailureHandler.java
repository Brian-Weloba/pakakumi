package tech.saturdev.pakakumi.security;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        System.out.println(exception);
        String errorMessage = "Invalid username or password";
        if (exception instanceof BadCredentialsException) {
            errorMessage = "Invalid username or password";
        } else if (exception instanceof LockedException) {
            errorMessage = "Account locked";
        } else if (exception instanceof DisabledException) {
            errorMessage = "Account disabled";
        } else if (exception instanceof AccountExpiredException) {
            errorMessage = "Account expired";
        } else if (exception instanceof CredentialsExpiredException) {
            errorMessage = "Credentials expired";
        } else if (exception instanceof AuthenticationServiceException) {
            exception.printStackTrace();
            errorMessage = "Authentication service error";
        }

        response.sendRedirect("/login?error=" + errorMessage);
    }
}
