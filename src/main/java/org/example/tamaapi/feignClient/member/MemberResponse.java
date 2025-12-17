package org.example.tamaapi.feignClient.member;

import lombok.Data;
import org.example.tamaapi.domain.user.Authority;
import org.example.tamaapi.domain.user.Member;

@Data
public class MemberResponse {

    private Long id;
    private String nickname;
    private Authority authority;

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        authority = member.getAuthority();
    }
}
