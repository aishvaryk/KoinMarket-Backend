package com.koinmarket.app.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "latest_quotes_usd")
public class LatestQuotesUSD {

    @Setter
    @Id
    @Column(name = "latest_listing_id")
    private Integer id;

    @Setter
    private Double price;

    @Setter
    @Column (name = "volume_24h")
    private Double volume24H;

    @Setter
    @Column (name = "volume_change_24h")
    private Double volumeChange24H;

    @Setter
    @Column (name = "percent_change_1h")
    private Double percentChange1H;

    @Setter
    @Column (name = "percent_change_24h")
    private Double percentChange24H;

    @Setter
    @Column (name = "percent_change_7d")
    private Double percentChange7D;

    @Setter
    @Column (name = "percent_change_30d")
    private Double percentChange30D;

    @Setter
    @Column (name = "percent_change_60d")
    private Double percentChange60D;

    @Setter
    @Column (name = "percent_change_90d")
    private Double percentChange90D;

    @Setter
    private Double marketCap;

    @Setter
    private Double marketCapDominance;

    @Setter
    @Column (name = "fully_diluted_market_cap")
    private Double fullyDilutedMarketCap;

    @Setter
    private LocalDateTime lastUpdated;

    @OneToOne
    @JoinColumn(name = "latest_listing_id")
    @MapsId
    @Setter
    @JsonBackReference //helps with serialising object without infinite recursion
    private LatestListings latestListings;
}
