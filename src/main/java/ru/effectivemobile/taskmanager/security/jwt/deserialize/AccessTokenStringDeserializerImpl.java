package ru.effectivemobile.taskmanager.security.jwt.deserialize;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import ru.effectivemobile.taskmanager.security.jwt.AccessToken;

import java.text.ParseException;
import java.util.UUID;

@AllArgsConstructor
public class AccessTokenStringDeserializerImpl implements AccessTokenStringDeserializer {
    private final JWSVerifier jwsVerifier;

    @Override
    public AccessToken apply(String s) {
        try {
            var signed = SignedJWT.parse(s);
            if (signed.verify(jwsVerifier)) {
                JWTClaimsSet claimsAccess = signed.getJWTClaimsSet();
                return getAccessTokenFromClaims(claimsAccess);
            }
        } catch (ParseException | JOSEException ignored) {
        }
        return null;
    }

    private AccessToken getAccessTokenFromClaims(JWTClaimsSet claimsAccess) throws ParseException {
        return new AccessToken(
                UUID.fromString(claimsAccess.getJWTID()),
                claimsAccess.getSubject(),
                claimsAccess.getStringListClaim("authorities"),
                claimsAccess.getIssueTime().toInstant(),
                claimsAccess.getExpirationTime().toInstant());
    }
}
