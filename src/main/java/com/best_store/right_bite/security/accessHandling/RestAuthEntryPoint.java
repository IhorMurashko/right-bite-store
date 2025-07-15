package com.best_store.right_bite.security.accessHandling;

import com.best_store.right_bite.security.exception.SecurityExceptionMessageProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class RestAuthEntryPoint implements AuthenticationEntryPoint {
    /**
     * Commences an authentication scheme.
     * <p>
     * This method is called when an exception is thrown due to an unauthenticated user attempting to access a secured resource.
     * It logs the error and sends a 401 Unauthorized response to the client.
     *
     * @param request       the HttpServletRequest in which the exception occurred.
     * @param response      the HttpServletResponse to which the error response will be sent.
     * @param authException the exception that caused the invocation.
     * @throws IOException      if an input or output exception occurs.
     * @throws ServletException if a servlet-specific error occurs.
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        log.error(authException.getMessage(), authException);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(SecurityExceptionMessageProvider.UNAUTHORIZED_EXCEPTION_MESSAGE);
    }
}
