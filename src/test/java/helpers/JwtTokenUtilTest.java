package helpers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.movierating.helpers.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JwtTokenUtilTest {
    @Test
    void encryptAndDecryptJwt() {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil("TEST_SECRET");
        String encodedToken = jwtTokenUtil.createToken(10);
        Assertions.assertEquals("10", jwtTokenUtil.decodeToken(encodedToken));
    }

    @Test
    void failToDecryptJwtWithWrongSecret() {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil("TEST_SECRET");
        String encodedToken = jwtTokenUtil.createToken(10);
        JwtTokenUtil wrongKeyJwtTokenUtil = new JwtTokenUtil("WRONG_KEY");

        Assertions.assertThrows(SignatureVerificationException.class,
                () -> wrongKeyJwtTokenUtil.decodeToken(encodedToken));
    }

    @Test
    void failToDecryptUnencryptedJwt() {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil("TEST_SECRET");
        String plainToken = createPlainToken("10");

        Assertions.assertThrows(AlgorithmMismatchException.class,
                () -> jwtTokenUtil.decodeToken(plainToken));
    }

    private String createPlainToken(String id) {
        String token = JWT.create()
                .withIssuer("arcanecat.com")
                .withClaim("user_id", id)
                .sign(Algorithm.none());
        return token;
    }
}
