package com.app.employee.security;

import com.app.employee.constant.Enum;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LogManager.getLogger(JwtTokenProvider.class);

    @Value("${security.jwt.token.secret-key-A128CBC_HS256}")
    private String secretKeyA128CBC_HS256;

    public static KeyPair RSA_KEYS = null;

    static {
        try {
            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
            keyGenerator.initialize(2048, new SecureRandom(Enum.KEY_ID.getBytes()));
            RSA_KEYS = keyGenerator.genKeyPair();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public String createTokenWithRSA(String userName, List<String> roles) {
        SignedJWT signedJWT = getSignedJWT(userName, roles);
        // Create JWE object with signed JWT as payload
        //with DirectEncrypter
        JWEObject jweObject = new JWEObject((new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256))
                .contentType("JWT").keyID(Enum.ENCRYPTION_KEY_ID).build(), new Payload(signedJWT));

        try {
            // Perform encryption
            byte[] secretKey = secretKeyA128CBC_HS256.getBytes();
            jweObject.encrypt(new DirectEncrypter(secretKey));
        } catch (JOSEException e) {
            logger.error(e.toString());
        }
        // Serialize to JWE compact form
        String token = jweObject.serialize();

        if (logger.isTraceEnabled()) {
            logger.trace("Create token: {}", token);
        }
        return token;
    }

    public static SignedJWT getSignedJWT(String userName, List<String> roles) {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneOffset.UTC);
        // Prepare JWT with claims set
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(userName)
                .notBeforeTime(Date.from(currentTime.minusMinutes(Enum.EXPIRATION_TIME_MINUTES).toInstant()))
                .expirationTime(Date.from(currentTime.plusMinutes(Enum.EXPIRATION_TIME_MINUTES).toInstant()))
                .issueTime(Date.from(currentTime.toInstant()))
                .claim("roles", roles)
                .jwtID(UUID.randomUUID().toString()).build();

        JWK jwk = new RSAKey.Builder((RSAPublicKey) RSA_KEYS.getPublic()).keyID(Enum.KEY_ID).build();
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(Enum.KEY_ID).jwk(jwk).build();
        SignedJWT signedJWT = new SignedJWT(jwsHeader, claims);
        try {
            JWSSigner signer = new RSASSASigner(RSA_KEYS.getPrivate());
            signedJWT.sign(signer);
        } catch (JOSEException e) {
            logger.error(e.toString());
        }
        return signedJWT;
    }

    /**
     * Encapsulate the acquisition of the JWT token from Headers.
     *
     * @param req servlet request to get the JWT token from
     * @return bearerToken
     */
    public String resolveToken(ServletRequest req) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) req;
        String bearerToken = httpServletRequest.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * This method provides decrypt token and validte token.
     *
     * @return true if valid
     * @paramjwtToken the token to validate
     */
    public boolean validateTokenWithRSA(String token) {
        // Decrypt Token
        JWEObject jweObject = decryptToken(token);
        SignedJWT signedJWT = jweObject.getPayload().toSignedJWT();
        // Validate Token
        return validateSignature(signedJWT);
    }

    /**
     * Verify the signature of the JWT token in this method. This method depends on
     * the public key that was established during init based upon the provisioned
     * public key. Override this method in subclasses in order to customize the
     * signature verification behavior.
     *
     * @param signedJWT the token that contains the signature to be validated
     * @return valid true if signature verifies successfully; false otherwise
     */
    private boolean validateSignature(SignedJWT signedJWT) {
        boolean valid = false;
        if (JWSObject.State.SIGNED == signedJWT.getState()) {
            // JWT token is in a SIGNED state.
            if (signedJWT.getSignature() != null) {
                try {
                    JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) RSA_KEYS.getPublic());
                    if (signedJWT.verify(verifier)) {
                        // Token is valid
                        // Validate Expiration
                        valid = validateExpiration(signedJWT);
                        if (!valid) {
                            throw new RuntimeException(Enum.EXPIRED_TOKEN);
                        }
                    } else {
                        logger.info("Validation Failed.");
                    }
                } catch (JOSEException e) {
                    logger.error(e.getMessage());
                    throw new RuntimeException(Enum.EXPIRED_INVALID_TOKEN);
                }
            }
        }
        return valid;
    }

    public JWEObject decryptToken(String token) {
        try {
            JWEObject jweObject = JWEObject.parse(token);
            // Decryption with DirectDecrypter
            byte[] secretKey = secretKeyA128CBC_HS256.getBytes();
            jweObject.decrypt(new DirectDecrypter(secretKey));
            return jweObject;
            // Decryption with RSADecrypter
            // jweObject.decrypt(new RSADecrypter(RSA_KEYS.getPrivate()));
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(Enum.ERROR_DECRYPT_TOKEN);
        }
    }

    /**
     * Validate that the expiration time of the JWT token.
     *
     * @return valid true if the token has not expired; false otherwise
     * @paramjwtToken the token that contains the expiration date to validate
     */
    private boolean validateExpiration(SignedJWT signedJWT) {
        boolean valid = false;
        try {
            Date expires = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expires != null && Instant.now().isBefore(expires.toInstant())) {
                return true;
            }
            logger.info("Expried Token.");
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return valid;
    }

    public Authentication getAuthentication(String token) {
        // Decrypt Token
        JWEObject jweObject = decryptToken(token);
        SignedJWT signedJWT = jweObject.getPayload().toSignedJWT();

        String user = getUserNameFromJWT(signedJWT);
        List<SimpleGrantedAuthority> authorities = getRolesFromJWT(signedJWT);
        return new UsernamePasswordAuthenticationToken(user, "", authorities);
    }

    public String getUserNameFromJWT(SignedJWT signedJWT) {
        String userName = null;
        try {
            userName = signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return userName;
    }

    @SuppressWarnings("unchecked")
    public List<SimpleGrantedAuthority> getRolesFromJWT(SignedJWT signedJWT) {

        try {
            List<String> roles = (List<String>) signedJWT.getJWTClaimsSet().getClaim("roles");
            if (roles != null) {
                return roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


}
