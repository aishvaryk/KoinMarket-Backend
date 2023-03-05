package com.koinmarket.app.controllers;

import com.koinmarket.app.entities.LatestQuotesUSD;
import com.koinmarket.app.services.LatestQuotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quotes")
public class LatestQuotesController{
    @Autowired
    private LatestQuotesService latestQuotesService;

    @GetMapping
    public ResponseEntity<List<LatestQuotesUSD>> getQuotesById(@RequestParam() List<Integer> ids) {
        return latestQuotesService.getQuotesById(ids);
    }
}
