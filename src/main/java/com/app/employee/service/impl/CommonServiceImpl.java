package com.app.employee.service.impl;

import com.app.employee.security.JwtTokenProvider;
import com.app.employee.service.CommonService;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class CommonServiceImpl implements CommonService {
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public CommonServiceImpl(JwtTokenProvider jwtTokenProvider) {
        super();
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Optional<String> getCurrentUser(HttpServletRequest httpServletRequest) {
        Optional<String> header = Optional.ofNullable(httpServletRequest.getHeader("Authorization"));
        if (header.isPresent()) {
            String bearerToken = (String) header.get();
            if (bearerToken.startsWith("Bearer ")) {
                String token = bearerToken.substring(7);
                JWEObject jweObject = jwtTokenProvider.decryptToken(token);
                SignedJWT signedJWT = jweObject.getPayload().toSignedJWT();
                String currentUser = jwtTokenProvider.getUserNameFromJWT(signedJWT);
                return Optional.ofNullable(currentUser);
            }
        }
        return Optional.empty();
    }
}
