package com.koinmarket.app.controllers;

import com.koinmarket.app.dtos.ListingDTO;
import com.koinmarket.app.entities.LatestListings;
import com.koinmarket.app.services.LatestListingService;
import com.koinmarket.app.services.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/list")
public class LatestListingController {

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private LatestListingService latestListingService;

    @GetMapping
    public List<ListingDTO> getListOfCoins(@RequestParam int pageNo, @RequestParam int count, @RequestParam(required = false, defaultValue = "rank") String orderBy, @RequestParam(required = false, defaultValue = "asc") String direction) {
        List<LatestListings> latestListingsEntities;
        List<ListingDTO> latestListingDTOs = new ArrayList<>();

        if (direction.equals("desc")) {
            latestListingsEntities = latestListingService.getListOfCoins(pageNo, count, orderBy, Sort.Direction.DESC);
        } else {
            latestListingsEntities =  latestListingService.getListOfCoins(pageNo, count, orderBy, Sort.Direction.ASC);
        }
        latestListingsEntities.forEach(latestListingsEntity -> {
            ListingDTO latestListingDTO = new ListingDTO(latestListingsEntity);
            if (latestListingDTO.getLogoURL()==null) {
                latestListingDTO.setLogoURL(metadataService.getMetadataById(Collections.singletonList(latestListingDTO.getId())).get(0).getLogoURL());
            }
            latestListingDTOs.add(latestListingDTO);
        });
        return latestListingDTOs;
    }
}
