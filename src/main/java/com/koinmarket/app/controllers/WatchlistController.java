package com.koinmarket.app.controllers;

import com.koinmarket.app.dtos.ListingDTO;
import com.koinmarket.app.dtos.WatchlistDTO;
import com.koinmarket.app.entities.Watchlist;
import com.koinmarket.app.services.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/watchlists")
public class WatchlistController {

    @Autowired
    private WatchlistService watchlistService;

    @Autowired
    private LatestListingController listingController;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/watchlist/{id}")
    public ResponseEntity<WatchlistDTO> getWatchlist(@PathVariable("id") Integer id) {
        Watchlist watchlistEntity =  watchlistService.getWatchlistById(id);
        WatchlistDTO watchlistDTO = convertWatchlistEntityToDTO(watchlistEntity);
        return ResponseEntity.ok().body(watchlistDTO);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/all")
    public ResponseEntity<List<WatchlistDTO> >getAllWatchlistsByUser() {
        List<Watchlist> watchlistEntityList = watchlistService.getAllWatchlistsByUser();
        List<WatchlistDTO> watchlistDTOList = new ArrayList<>();
        watchlistEntityList.forEach((watchlistEntity) -> {
            watchlistDTOList.add(convertWatchlistEntityToDTO(watchlistEntity));
        });
        return ResponseEntity.ok().body(watchlistDTOList);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/add")
    public ResponseEntity<WatchlistDTO> addWatchlistByUser(@RequestParam String name) {
        Watchlist watchlistEntity =  watchlistService.addWatchlistByUser(name);
        WatchlistDTO watchlistDTO = convertWatchlistEntityToDTO(watchlistEntity);
        return ResponseEntity.ok().body(watchlistDTO);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping("/watchlist/{id}/add")
    public ResponseEntity<WatchlistDTO> addItemToWatchList(@PathVariable("id") Integer id, @RequestParam Integer coinId) {
        Watchlist watchlistEntity = watchlistService.addItemToWatchList(id, coinId);
        WatchlistDTO watchlistDTO = convertWatchlistEntityToDTO(watchlistEntity);
        return ResponseEntity.ok().body(watchlistDTO);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping("/watchlist/{id}/remove")
    public ResponseEntity<WatchlistDTO> removeItemFromWatchList(@PathVariable("id") Integer id, @RequestParam Integer coinId) {
        Watchlist watchlistEntity =  watchlistService.removeItemFromWatchList(id, coinId);
        WatchlistDTO watchlistDTO = convertWatchlistEntityToDTO(watchlistEntity);
        return ResponseEntity.ok().body(watchlistDTO);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> removeWatchlist(@RequestParam Integer id) {
        HttpStatus httpStatus = watchlistService.removeWatchlistByUser(id);
        return ResponseEntity.ok().body(httpStatus);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public WatchlistDTO convertWatchlistEntityToDTO(Watchlist watchlist) {
        Integer id = watchlist.getId();
        String name = watchlist.getName();
        HashSet<ListingDTO> listingDTOs = new HashSet<>();
        watchlist.getLatestListings().forEach((listingEntity)-> {
            listingDTOs.add(listingController.convertListEntityToDTO(listingEntity));
        });
        return new WatchlistDTO(id, name, listingDTOs);
    }
}
