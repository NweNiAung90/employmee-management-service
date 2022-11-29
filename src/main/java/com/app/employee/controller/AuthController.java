package com.app.employee.controller;

import com.app.employee.constant.Enum;
import com.app.employee.model.common.AuthErrorResponse;
import com.app.employee.model.common.AuthResponse;
import com.app.employee.model.dto.UserInfoDto;
import com.app.employee.model.request.LoginRequest;
import com.app.employee.model.response.ApiResponse;
import com.app.employee.model.response.TokenResponse;
import com.app.employee.security.JwtTokenProvider;
import com.app.employee.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LogManager.getLogger(AuthController.class);
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserInfoService userInfoService;

    @Operation(summary = "authentication login")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "503", description = "Service Unavailable",
                    content = @Content)})
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request,
                                   HttpServletResponse response) {
        if (loginRequest.getUserId().isEmpty() || loginRequest.getPassword().isEmpty()) {
            return new ResponseEntity(new AuthErrorResponse(Enum.USERNAME_OR_PASSWORD_INVALID),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            if (StringUtils.isEmpty(request.getHeader(Enum.SALT_FIELD.toLowerCase()))
                    || !request.getHeader(Enum.SALT_FIELD.toLowerCase())
                    .equalsIgnoreCase(Enum.SALT_VALUE)) {
                logger.warn("UNAUTHORIZED_USER" + loginRequest.getUserId());
                return new ResponseEntity(new AuthErrorResponse(Enum.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserId(), loginRequest.getPassword()));

            UserInfoDto userInfoDto;
            try {
                userInfoDto = userInfoService.getUserInfo(loginRequest.getUserId());
            } catch (Exception e) {
                logger.error("Add User Info Error : " + e.getMessage());
                throw e;
            }
            //check first time login or not
            if (authentication.isAuthenticated()) {
                AuthResponse authResponse;
                //  User existing and return generated token
                if (userInfoDto == null) {
                    // Generate Sercert Key for first time login
                    String authSecretKey = generateSecretKey();
                    authResponse = userInfoService.addUserInfoData(loginRequest, authSecretKey);
                    return new ResponseEntity(authResponse, HttpStatus.OK);

                } else {
                    List<String> roles = new ArrayList<String>();
                    for (GrantedAuthority ga : authentication.getAuthorities()) {
                        roles.add(ga.getAuthority());
                    }
                    String token = jwtTokenProvider.createTokenWithRSA(loginRequest.getUserId(), roles);

                    TokenResponse tokenResponse = new TokenResponse();
                    tokenResponse.setAccessToken(token);
                    tokenResponse.setTokenType("Bearer");

                    Cookie cookie = new Cookie("Authorization", token);
                    cookie.setPath("/");
                    response.addCookie(cookie);

                    return new ResponseEntity(tokenResponse, HttpStatus.OK);
                }

            } else {
                if (userInfoDto == null) {
                    //secret key is not exist in database.
                    return new ResponseEntity(new AuthErrorResponse(Enum.INVALID_SECRET_KEY),
                            HttpStatus.UNAUTHORIZED);
                }
            }
        } catch (AuthenticationException e) {
            logger.error("AuthenticationException : " + e.getMessage(), e);
            return new ResponseEntity(new AuthErrorResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
        } catch (HttpServerErrorException e) {
            return new ResponseEntity(new AuthErrorResponse(e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            logger.error("Exception : " + e.getMessage());
            return new ResponseEntity(new AuthErrorResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

    private String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[10];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

}
