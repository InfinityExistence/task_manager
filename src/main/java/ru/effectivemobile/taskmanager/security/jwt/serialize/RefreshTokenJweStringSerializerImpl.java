package ru.effectivemobile.taskmanager.security.jwt.serialize;

import com.nimbusds.jose.*;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.effectivemobile.taskmanager.security.jwt.RefreshToken;

import java.util.Date;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
public class RefreshTokenJweStringSerializerImpl implements RefreshTokenJweStringSerializer {
    private final JWEEncrypter jweEncrypter;
    private JWEAlgorithm jweAlgorithm = JWEAlgorithm.DIR;
    private EncryptionMethod encryptionMethod = EncryptionMethod.A128GCM;
    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenJweStringSerializerImpl.class);

    @Override
    public String apply(RefreshToken token) {
        var jweHeader = getJweHeader(token);
        var claimsSet = getJwtClaimsSet(token);
        var encryptedJWT = new EncryptedJWT(jweHeader, claimsSet);
        try {
            encryptedJWT.encrypt(jweEncrypter);
        } catch (JOSEException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
        return encryptedJWT.serialize();
    }

    private static JWTClaimsSet getJwtClaimsSet(RefreshToken token) {
        return new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .subject(token.subject())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiresAt()))
                .claim("authorities", token.authorities())
                .build();
    }

    private JWEHeader getJweHeader(RefreshToken token) {
        return new JWEHeader.Builder(jweAlgorithm, encryptionMethod)
                .keyID(token.id().toString())
                .build();
    }
}
