package com.koinmarket.app.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "latest_quotes_usd")
public class LatestQuotesUSD {

    @Getter @Setter
    @Id
    @Column(name = "latest_listing_id")
    private Integer id;

    @Getter @Setter
    private Double price;

    @Getter @Setter
    @Column (name = "volume_24h")
    private Double volume24H;

    @Getter @Setter
    @Column (name = "volume_change_24h")
    private Double volumeChange24H;

    @Getter @Setter
    @Column (name = "percent_change_1h")
    private Double percentChange1H;

    @Getter @Setter
    @Column (name = "percent_change_24h")
    private Double percentChange24H;

    @Getter @Setter
    @Column (name = "percent_change_7d")
    private Double percentChange7D;

    @Getter @Setter
    @Column (name = "percent_change_30d")
    private Double percentChange30D;

    @Getter @Setter
    @Column (name = "percent_change_60d")
    private Double percentChange60D;

    @Getter @Setter
    @Column (name = "percent_change_90d")
    private Double percentChange90D;

    @Getter @Setter
    private Double marketCap;

    @Getter @Setter
    private Double marketCapDominance;

    @Getter @Setter
    @Column (name = "fully_diluted_market_cap")
    private Double fullyDilutedMarketCap;

    @Getter @Setter
    private LocalDateTime lastUpdated;

    @OneToOne
    @JoinColumn(name = "latest_listing_id")
    @MapsId
    @Setter @Getter
    @JsonBackReference
    private LatestListings latestListings;

    public LatestQuotesUSD() {
        super();
    }
}
