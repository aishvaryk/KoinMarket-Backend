package com.koinmarket.app.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
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
    @JsonManagedReference //helps with serialising object without infinite recursion
    private LatestQuotesUSD quotesUSD;

    @OneToOne(mappedBy = "latestListings" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference //helps with serialising object without infinite recursion
    private Metadata metadata;

    public void setQuotesUSD(LatestQuotesUSD quotesUSD) {
        this.quotesUSD = quotesUSD;
        this.quotesUSD.setLatestListings(this);
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
        this.metadata.setLatestListings(this);
    }

}
