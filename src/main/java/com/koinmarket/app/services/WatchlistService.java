package com.koinmarket.app.services;

import com.koinmarket.app.entities.LatestListings;
import com.koinmarket.app.entities.User;
import com.koinmarket.app.entities.Watchlist;
import com.koinmarket.app.exceptions.listings.ListingsNotFoundException;
import com.koinmarket.app.exceptions.watchlist.WatchlistNotFoundException;
import com.koinmarket.app.repositories.LatestListingRepository;
import com.koinmarket.app.repositories.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchlistService {

    @Autowired
    private LatestListingRepository listingRepository;

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private  MyUserService userService;

    public List<Watchlist> getAllWatchlistsByUser() {
        User currentUser = userService.currentUser();
        List<Watchlist> watchlists = watchlistRepository.findAllByUser(currentUser);
        return watchlists;
    }


    public Watchlist getWatchlistById(Integer id) {
        Watchlist watchlist = watchlistRepository.findById(id).orElse(null);
        return watchlist;
    }

    public Watchlist addWatchlistByUser(String name) {
        User currentUser = userService.currentUser();
        Watchlist watchlist = new Watchlist();
        watchlist.setName(name);
        currentUser.addWatchlist(watchlist);
        watchlist.setUser(currentUser);
        Watchlist watchlistResponse = watchlistRepository.save(watchlist);
        return watchlistResponse;
    }

    public Watchlist addItemToWatchList(Integer watchlistId, Integer coinId) {
        Watchlist watchlist = watchlistRepository.findById(watchlistId).orElse(null);
        if (watchlist==null) {
            throw new WatchlistNotFoundException();
        }
        LatestListings listing = listingRepository.findById(coinId).orElse(null);
        if (listing == null) {
            throw new ListingsNotFoundException();
        }
        watchlist.addCoin(listing);
        watchlistRepository.save(watchlist);
        return watchlist;
    }

    public Watchlist removeItemFromWatchList(Integer watchlistId, Integer coinId) {
        Watchlist watchlist = watchlistRepository.findById(watchlistId).orElse(null);
        if (watchlist==null) {
            throw new WatchlistNotFoundException();
        }
        LatestListings listing = listingRepository.findById(coinId).orElse(null);
        if (listing == null) {
            throw new ListingsNotFoundException();
        }
        watchlist.removeCoin(listing);
        watchlistRepository.save(watchlist);
        return watchlist;
    }

    public HttpStatus removeWatchlistByUser(Integer watchlistId) {
        User currentUser = userService.currentUser();
        Watchlist watchlist = watchlistRepository.findById(watchlistId).orElse(null);
        if (watchlist==null) {
            throw new WatchlistNotFoundException();
        }
        currentUser.removeWatchlist(watchlist);
        watchlistRepository.deleteById(watchlistId);
        return HttpStatus.OK;
    }
}
