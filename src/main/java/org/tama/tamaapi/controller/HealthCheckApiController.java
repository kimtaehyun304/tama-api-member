package org.tama.tamaapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HealthCheckApiController {

    @GetMapping("/api/member/health-check/ok")
    public String healthOk() {
        log.info("ok");
        return "ok";
    }

    @GetMapping("/api/member/health-check/error")
    public String healthError() {
        throw new RuntimeException("error");
    }

}
