package com.nitheesh.pastebin.controller;

import com.nitheesh.pastebin.model.Paste;
import com.nitheesh.pastebin.service.PasteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PasteViewController {

    private final PasteService service;

    public PasteViewController(PasteService service) {
        this.service = service;
    }

    @GetMapping("/p/{id}")
    public String view(@PathVariable String id, HttpServletRequest request, Model model) {
        Paste paste = service.fetch(id, request);
        model.addAttribute("content", paste.getContent());
        return "paste";
    }
}
