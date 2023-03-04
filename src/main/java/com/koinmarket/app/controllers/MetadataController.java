package com.koinmarket.app.controllers;

import com.koinmarket.app.entities.Metadata;
import com.koinmarket.app.services.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/metadata")
public class MetadataController {
    @Autowired
    private MetadataService metadataService;

    @GetMapping("/{id}")
    public ResponseEntity<Metadata> getMetadataById(@PathVariable(value = "id") int id) {
        return metadataService.getMetadataById(id);
    }
}
