package com.nitheesh.pastebin.repository;

import com.nitheesh.pastebin.model.Paste;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasteRepository extends JpaRepository<Paste, String> { }
