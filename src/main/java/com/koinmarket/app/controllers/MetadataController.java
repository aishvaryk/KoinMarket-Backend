package com.koinmarket.app.controllers;

import com.koinmarket.app.entities.Metadata;
import com.koinmarket.app.services.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/metadata")
public class MetadataController {
    @Autowired
    private MetadataService metadataService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Metadata>> getMetadataById(@RequestParam() List<Integer> ids) {
        return ResponseEntity.ok().body(metadataService.getMetadataById(ids));
    }
}
