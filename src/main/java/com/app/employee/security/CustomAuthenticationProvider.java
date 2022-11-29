package com.app.employee.security;

import com.app.employee.constant.Enum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    private final AuthenticationManager authManager;


    public CustomAuthenticationProvider(AuthenticationManager authManager) {
        super();
        this.authManager = authManager;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        List<SimpleGrantedAuthority> grantedAuth = null;
        UsernamePasswordAuthenticationToken token = null;

        try {
            //For Employee User
            Collection<String> roles = Arrays.asList(Enum.ROLE_GROUP_USER, Enum.ROLE_USER);
            grantedAuth = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            token = new UsernamePasswordAuthenticationToken(authentication.getPrincipal().toString(), authentication.getCredentials().toString(), grantedAuth);
            token.setDetails(authentication);
            SecurityContextHolder.getContext().setAuthentication(token);
            return token;
        } catch (HttpServerErrorException exception) {
            throw exception;
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            throw new BadCredentialsException(Enum.INCORRECT_CREDENTIALS);
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
