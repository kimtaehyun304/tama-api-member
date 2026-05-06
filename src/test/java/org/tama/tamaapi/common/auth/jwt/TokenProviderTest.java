package org.tama.tamaapi.common.auth.jwt;

import org.tama.tamaapi.config.TokenGenerator;
import org.tama.tamaapi.domain.user.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TokenProviderTest {

    @Autowired
    private TokenGenerator tokenGenerator;

    //테스트할때 하드코딩된 jwt 필요해서 알아내려고
    @Test
    void generateToken() {
        String token = tokenGenerator.generateTestToken(new Member(3L));
        System.out.println("token = " + token);
    }
}