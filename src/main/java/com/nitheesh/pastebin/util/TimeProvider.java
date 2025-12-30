package com.nitheesh.pastebin.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TimeProvider {

    public Instant now(HttpServletRequest request) {
        if ("1".equals(System.getenv("TEST_MODE"))) {
            String header = request.getHeader("x-test-now-ms");
            if (header != null) {
                return Instant.ofEpochMilli(Long.parseLong(header));
            }
        }
        return Instant.now();
    }
}
