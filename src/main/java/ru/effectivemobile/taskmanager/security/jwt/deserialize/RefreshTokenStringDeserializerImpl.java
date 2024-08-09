package ru.effectivemobile.taskmanager.security.jwt.deserialize;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.effectivemobile.taskmanager.security.jwt.RefreshToken;

import java.text.ParseException;
import java.util.UUID;

@RequiredArgsConstructor
public class RefreshTokenStringDeserializerImpl implements RefreshTokenStringDeserializer {
    private final JWEDecrypter jweDecrypter;
    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenStringDeserializerImpl.class);

    @Override
    public RefreshToken apply(String s) {
        try {
            var encryptedJWT = EncryptedJWT.parse(s);
            encryptedJWT.decrypt(jweDecrypter);
            var claimsRefresh = encryptedJWT.getJWTClaimsSet();
            return getRefreshTokenFromClaims(claimsRefresh);
        } catch (ParseException | JOSEException ignored) {
        }
        return null;
    }

    private static RefreshToken getRefreshTokenFromClaims(JWTClaimsSet claimsRefresh) throws ParseException {
        return new RefreshToken(
                UUID.fromString(claimsRefresh.getJWTID()),
                claimsRefresh.getSubject(),
                claimsRefresh.getStringListClaim("authorities"),
                claimsRefresh.getIssueTime().toInstant(),
                claimsRefresh.getExpirationTime().toInstant());
    }
}
