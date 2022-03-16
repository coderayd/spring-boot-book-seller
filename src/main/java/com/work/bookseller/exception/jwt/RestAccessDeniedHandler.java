package com.work.bookseller.exception.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.work.bookseller.exception.ErrorObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

// Kaynak
// https://stackoverflow.com/questions/19767267/handle-spring-security-authentication-exceptions-with-exceptionhandler

//@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    // Bu şekilde yapılırsa @RestControllerAdvice içinde yakalanabiliyorlar.
//    @Autowired
//    @Qualifier("handlerExceptionResolver")
//    private HandlerExceptionResolver resolver;
//
//    @Override
//    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
//        resolver.resolveException(httpServletRequest, httpServletResponse, null, e);
//    }

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {

        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ErrorObject message = new ErrorObject(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                e.getMessage(),
                "Unauthorized access has occurred and Access denied"
        );
        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, message);
        out.flush();
    }
}