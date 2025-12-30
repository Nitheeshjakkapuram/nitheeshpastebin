package com.nitheesh.pastebin.service;

import com.nitheesh.pastebin.model.Paste;
import com.nitheesh.pastebin.repository.PasteRepository;
import com.nitheesh.pastebin.util.TimeProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.UUID;

@Service
public class PasteService {

    private final PasteRepository repository;
    private final TimeProvider timeProvider;

    public PasteService(PasteRepository repository, TimeProvider timeProvider) {
        this.repository = repository;
        this.timeProvider = timeProvider;
    }

    public Paste create(String content, Integer ttlSeconds, Integer maxViews) {
        Paste paste = new Paste();
        paste.setId(UUID.randomUUID().toString());
        paste.setContent(content);
        paste.setMaxViews(maxViews);
        paste.setViewCount(0);

        if (ttlSeconds != null) {
            paste.setExpiresAt(Instant.now().plusSeconds(ttlSeconds));
        }

        return repository.save(paste);
    }

    public Paste fetch(String id, HttpServletRequest request) {
        Paste paste = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paste not found"));

        Instant now = timeProvider.now(request);

        if (paste.getExpiresAt() != null && now.isAfter(paste.getExpiresAt())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paste expired");
        }

        if (paste.getMaxViews() != null && paste.getViewCount() >= paste.getMaxViews()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paste view limit reached");
        }

        paste.setViewCount(paste.getViewCount() + 1);
        return repository.save(paste);
    }
}
