package com.hoang.vaultly.modules.auth.service;

import com.hoang.vaultly.common.exception.AppException;
import com.hoang.vaultly.common.exception.ErrorCode;
import com.hoang.vaultly.modules.user.entity.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    public static final String ACCESS_TYPE = "ACCESS";
    public static final String REFRESH_TYPE = "REFRESH";

    @Getter
    @Value("${jwt.access-token-duration}")
    private long accessTokenDuration;

    @Value("${jwt.refresh-token-duration}")
    private long refreshTokenDuration;

    @Value("${jwt.signer-key}")
    private String secretKey;

    @Value("${jwt.issuer}")
    private String issuer;


    public String generateAccessToken(User user) {
        return buildToken(user, accessTokenDuration, ACCESS_TYPE);
    }

    public String generateRefreshToken(User user) {
        return buildToken(user, refreshTokenDuration, REFRESH_TYPE);
    }

    private String buildToken(User user, long validDuration, String tokenType) {
        Instant now = Instant.now();

        // create header
        JWSHeader header = new JWSHeader
                .Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .build();

        // create claim
        Date issueAt = Date.from(now);
        Date expiryTime = Date.from(now.plusSeconds(validDuration));
        JWTClaimsSet.Builder claims = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer(issuer)
                .issueTime(issueAt)
                .expirationTime(expiryTime)
                .jwtID(UUID.randomUUID().toString())
                .claim("token_type", tokenType);

        // add roles / authority to access token
        if (ACCESS_TYPE.equals(tokenType)) {
            claims.claim("roles", user.getSystemRole().toString());
        }

        // sign jwt
        SignedJWT signedJWT = new SignedJWT(header, claims.build());
        try {
            JWSSigner signer = new MACSigner(secretKey.getBytes(StandardCharsets.UTF_8));
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new IllegalStateException("Fail to generate JWT", e);
        }
    }

    public JWTClaimsSet validateToken(String token, String expectedType) {
        // parse token
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            // verify algorithm
            if (!JWSAlgorithm.HS256.equals(signedJWT.getHeader().getAlgorithm())) {
                throw new AppException(ErrorCode.INVALID_TOKEN);
            }

            // verify signature
            JWSVerifier verifier = new MACVerifier(secretKey.getBytes(StandardCharsets.UTF_8));
            if (!signedJWT.verify(verifier))
                throw new AppException(ErrorCode.INVALID_TOKEN);

            // verify expiration time
            Date expiration = claimsSet.getExpirationTime();
            if (expiration == null || expiration.before(new Date()))
                throw new AppException(ErrorCode.EXPIRED_TOKEN);

            // verify issuer
            if (!issuer.equals(claimsSet.getIssuer()))
                throw new AppException(ErrorCode.INVALID_TOKEN);

            if (!expectedType.equals(claimsSet.getStringClaim("token_type")))
                throw new AppException(ErrorCode.INVALID_TOKEN);

            return claimsSet;
        } catch (ParseException | JOSEException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

}
