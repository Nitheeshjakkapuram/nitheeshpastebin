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
    private final String BASE_URL = "https://nitheeshpastebin-4.onrender.com"; // Change to your deployed URL

    public PasteApiController(PasteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> body) {
        String content = (String) body.get("content");
        Integer ttl = body.get("ttl_seconds") instanceof Number ? ((Number) body.get("ttl_seconds")).intValue() : null;
        Integer maxViews = body.get("max_views") instanceof Number ? ((Number) body.get("max_views")).intValue() : null;

        if (content == null || content.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid content"));
        }

        Paste paste = service.create(content, ttl, maxViews);

        String fullUrl = BASE_URL + "/p/" + paste.getId();

        return ResponseEntity.ok(Map.of(
                "id", paste.getId(),
                "url", fullUrl
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> fetch(@PathVariable String id, HttpServletRequest request) {
        Paste paste = service.fetch(id, request);

        Integer remainingViews = paste.getMaxViews() == null
                ? null
                : paste.getMaxViews() - paste.getViewCount();

        return ResponseEntity.ok(Map.of(
                "content", paste.getContent(),
                "remaining_views", remainingViews,
                "expires_at", paste.getExpiresAt()
        ));
    }
}
