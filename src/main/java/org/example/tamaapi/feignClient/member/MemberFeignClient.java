package org.example.tamaapi.feignClient.member;

import org.example.tamaapi.common.auth.CustomPrincipal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "member-service", url = "http://localhost:5003")
public interface MemberFeignClient {


    @GetMapping("/api/member/authority")
    Authority findAuthority(@RequestHeader("Authorization") String bearerJwt);



}
