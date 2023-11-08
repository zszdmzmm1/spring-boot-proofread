package com.auefly.spring.boot.security.controller.advice;

import com.auefly.spring.boot.security.bean.backend.BackendProperties;
import com.auefly.spring.boot.security.bean.backend.Menus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private BackendProperties backendProperties;

    @ModelAttribute("requestURI")
    public String getRequestURI() {
        return request.getRequestURI();
    }

    @ModelAttribute("menus")
    public List<Menus> getMenus(){
        return backendProperties.getMenus();
    }

}
