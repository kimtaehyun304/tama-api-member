package org.example.tamaapi.common.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.Collection;

@Getter
public class CustomPrincipal {
    private final String bearerJwt;
    private final Long memberId;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomPrincipal(String bearerJwt, Long userId, Collection<? extends GrantedAuthority> authorities) {
        this.bearerJwt = bearerJwt;
        this.memberId = userId;
        this.authorities = authorities;
    }

<<<<<<< HEAD
<<<<<<< HEAD
}
=======
}
>>>>>>> b5c94cf684565bcfb12724553ffc7966857c3c69
=======
}
>>>>>>> b5c94cf684565bcfb12724553ffc7966857c3c69
