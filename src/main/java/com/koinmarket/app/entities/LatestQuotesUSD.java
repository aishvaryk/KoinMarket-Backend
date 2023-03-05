package com.koinmarket.app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "latest_quotes_usd")
public class LatestQuotesUSD {

    @Id
    @Getter @Setter
    private int id;

    @Getter @Setter
    private double price;

    @Getter @Setter
    @Column (name = "volume_24h")
    private double volume24H;

    @Getter @Setter
    @Column (name = "volume_change_24h")
    private double volumeChange24H;

    @Getter @Setter
    @Column (name = "percent_change_1h")
    private double percentChange1H;

    @Getter @Setter
    @Column (name = "percent_change_24h")
    private double percentChange24H;

    @Getter @Setter
    @Column (name = "percent_change_7d")
    private double percentChange7D;

    @Getter @Setter
    @Column (name = "percent_change_30d")
    private double percentChange30D;

    @Getter @Setter
    @Column (name = "percent_change_60d")
    private double percentChange60D;

    @Getter @Setter
    @Column (name = "percent_change_90d")
    private double percentChange90D;

    @Getter @Setter
    private double marketCap;

    @Getter @Setter
    private double marketCapDominance;

    @Getter @Setter
    @Column (name = "fully_diluted_market_cap")
    private double fullyDilutedMarketCap;

    @Getter @Setter
    private String lastUpdated;

    public LatestQuotesUSD() {}

    public LatestQuotesUSD(int id, double price, double volume24H, double percentChange1H, double percentChange24H, double percentChange7D, double percentChange30D, double percentChange60D, double percentChange90D, double marketCap, double marketCapDominance, double fullyDilutedMarketCap, String lastUpdated) {
        this.setId(id);
        this.setPrice(price);
        this.setVolume24H(volume24H);
        this.setPercentChange1H(percentChange1H);
        this.setPercentChange24H(percentChange24H);
        this.setPercentChange7D(percentChange7D);
        this.setPercentChange30D(percentChange30D);
        this.setPercentChange60D(percentChange60D);
        this.setPercentChange90D(percentChange90D);
        this.setMarketCap(marketCap);
        this.setMarketCapDominance(marketCapDominance);
        this.setFullyDilutedMarketCap(fullyDilutedMarketCap);
        this.setLastUpdated(lastUpdated);
    }
}
