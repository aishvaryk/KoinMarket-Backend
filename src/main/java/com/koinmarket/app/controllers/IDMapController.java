package com.koinmarket.app.controllers;

import com.koinmarket.app.entities.IDMap;
import com.koinmarket.app.services.IDMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/list")
public class IDMapController {

    @Autowired
    private IDMapService idMapService;

    @GetMapping
    public List<IDMap> getListOfCoins(@RequestParam int pageNo, @RequestParam int count, @RequestParam(required = false, defaultValue = "rank") String orderBy, @RequestParam(required = false, defaultValue = "asc") String direction) {
        return idMapService.getListOfCoins(pageNo, count, orderBy, direction);
    }
}
