package com.koinmarket.app.dtos;

import com.koinmarket.app.entities.LatestListings;
import com.koinmarket.app.services.MetadataService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Data
public class ListingDTO {
    private Integer id;
    private String name;
    private String logoURL;
    private Double marketCap;
    private Double price;
    private Double circulatingSupply;
    private Double change24H;
    private Double change7D;

    public ListingDTO(LatestListings latestListings) {
        this.setId(latestListings.getId());
        this.setName(latestListings.getName());
        this.setMarketCap(latestListings.getQuotesUSD().getMarketCap());
        this.setPrice(latestListings.getQuotesUSD().getPrice());
        this.setCirculatingSupply(latestListings.getCirculatingSupply());
        this.setChange24H(latestListings.getQuotesUSD().getPercentChange24H());
        this.setChange7D(latestListings.getQuotesUSD().getPercentChange7D());
        if (latestListings.getMetadata()!=null) {
            this.setLogoURL(latestListings.getMetadata().getLogoURL());
        }
    }
}
