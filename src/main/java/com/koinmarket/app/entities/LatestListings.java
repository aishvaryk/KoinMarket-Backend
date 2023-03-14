package com.koinmarket.app.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
public class LatestListings {

    @Id
    @Setter
    private Integer id;

    @Setter
    private String name;

    @Setter
    private String symbol;

    @Setter
    private String slug;

    @Setter
    private Integer rank;

    @Setter
    private LocalDateTime lastUpdated;

    @Setter
    @Column(nullable = true)
    private Double maxSupply;

    @Setter
    @Column(nullable = true)
    private Double circulatingSupply;

    @Setter
    @Column(nullable = true)
    private Double totalSupply;

    @OneToOne(mappedBy = "latestListings" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private LatestQuotesUSD quotesUSD;

    @OneToOne(mappedBy = "latestListings" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Metadata metadata;

    public LatestListings() {
        super();
    }

    public void setQuotesUSD(LatestQuotesUSD quotesUSD) {
        this.quotesUSD = quotesUSD;
        this.quotesUSD.setLatestListings(this);
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
        this.metadata.setLatestListings(this);
    }

}
