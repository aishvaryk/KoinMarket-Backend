package com.koinmarket.app.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Watchlist {
    @GeneratedValue
    @Id
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REFRESH })
    @JoinTable(
        name = "watchlist_listing",
        joinColumns = { @JoinColumn(name = "watchlist_id") },
        inverseJoinColumns = { @JoinColumn(name = "listing_id") }
    )
    @JsonManagedReference
    private Set<LatestListings> latestListings = new HashSet<>();

    public Watchlist addCoin(LatestListings listing) {
        if (!this.latestListings.contains(listing)) {
            this.latestListings.add(listing);
        }
        return this;
    }

    public Boolean removeCoin(LatestListings listings) {
        return this.latestListings.remove(listings);

    }
}