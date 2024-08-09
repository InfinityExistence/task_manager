package ru.effectivemobile.taskmanager.security.jwt.serialize;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.effectivemobile.taskmanager.security.jwt.AccessToken;

import java.util.Date;

@RequiredArgsConstructor
@AllArgsConstructor
public class AccessTokenJwsStringSerializerImpl implements AccessTokenJwsStringSerializer {
    private final JWSSigner jwsSigner;
    @Setter
    private JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenJwsStringSerializerImpl.class);

    @Override
    public String apply(AccessToken token) {
        var jwsHeader = getJwsHeader(token);
        var claimsSet = getJwtClaimsSet(token);
        var signedJWT = new SignedJWT(jwsHeader, claimsSet);
        try {
            signedJWT.sign(jwsSigner);
        } catch (JOSEException e) {
            LOGGER.error(e.getMessage());
            return null;
        }

        return signedJWT.serialize();
    }

    private JWTClaimsSet getJwtClaimsSet(AccessToken token) {
        return new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .subject(token.subject())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiresAt()))
                .claim("authorities", token.authorities())
                .build();
    }

    private JWSHeader getJwsHeader(AccessToken token) {
        return new JWSHeader.Builder(jwsAlgorithm)
                .keyID(token.id().toString())
                .build();
    }
}
