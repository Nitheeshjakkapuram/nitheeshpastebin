package com.nitheesh.pastebin.controller;

import com.nitheesh.pastebin.model.Paste;
import com.nitheesh.pastebin.service.PasteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pastes")
public class PasteApiController {

    private final PasteService service;

    public PasteApiController(PasteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> body) {
        String content = (String) body.get("content");
        Integer ttl = (Integer) body.get("ttl_seconds");
        Integer maxViews = (Integer) body.get("max_views");

        if (content == null || content.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid content"));
        }

        Paste paste = service.create(content, ttl, maxViews);

        return ResponseEntity.ok(Map.of(
                "id", paste.getId(),
                "url", "/p/" + paste.getId()
        ));
    }

    @GetMapping("/{id}")
    public Map<String, Object> fetch(@PathVariable String id, HttpServletRequest request) {
        Paste paste = service.fetch(id, request);

        Integer remainingViews = paste.getMaxViews() == null
                ? null
                : paste.getMaxViews() - paste.getViewCount();

        return Map.of(
                "content", paste.getContent(),
                "remaining_views", remainingViews,
                "expires_at", paste.getExpiresAt()
        );
    }
}
