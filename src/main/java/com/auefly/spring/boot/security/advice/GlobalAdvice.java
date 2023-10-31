package com.auefly.spring.boot.security.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalAdvice {
    @Autowired
    private HttpServletRequest request;

    @ModelAttribute("requestURI")
    public String getRequestURI() {
        return request.getRequestURI();
    }

}
