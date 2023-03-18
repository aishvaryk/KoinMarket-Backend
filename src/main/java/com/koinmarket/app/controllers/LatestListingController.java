package com.koinmarket.app.controllers;

import com.koinmarket.app.entities.LatestListings;
import com.koinmarket.app.services.LatestListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/list")
public class LatestListingController {

    @Autowired
    private LatestListingService latestListingService;

    @GetMapping
    public List<LatestListings> getListOfCoins(@RequestParam int pageNo, @RequestParam int count, @RequestParam(required = false, defaultValue = "rank") String orderBy, @RequestParam(required = false, defaultValue = "asc") String direction) {
        if (direction.equals("desc")) {
            return latestListingService.getListOfCoins(pageNo, count, orderBy, Sort.Direction.DESC);
        } else {
            return latestListingService.getListOfCoins(pageNo, count, orderBy, Sort.Direction.ASC);
        }
    }
}
