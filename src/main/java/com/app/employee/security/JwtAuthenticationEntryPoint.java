package com.app.employee.security;

import com.app.employee.constant.Enum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LogManager.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        logger.error("Responding with unauthorized error. Message - {}", e.getMessage());

        final String expired = (String) httpServletRequest.getAttribute("expired");
        logger.info("Expired : " + expired);
        if (expired != null) {
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, Enum.INVAILD_TOKEN);
        } else {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, Enum.ACCESS_DENIED);
        }
    }
}