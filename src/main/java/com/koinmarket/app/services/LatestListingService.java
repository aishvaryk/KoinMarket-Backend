package com.koinmarket.app.services;

import com.koinmarket.app.configurations.application_properties.AppAPIConfiguration;
import com.koinmarket.app.entities.LatestListings;
import com.koinmarket.app.entities.LatestQuotesUSD;
import com.koinmarket.app.exceptions.CMCResponseErrorHandler;
import com.koinmarket.app.exceptions.listings.ListingsNotFetched;
import com.koinmarket.app.exceptions.listings.ListingsNotFoundException;
import com.koinmarket.app.repositories.LatestListingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class LatestListingService {

    @Autowired
    private LatestQuotesService quotesService;

    @Autowired
    private LatestListingRepository repository;
    @Autowired
    private AppAPIConfiguration config;

    @Scheduled(fixedRate = 3600000)
    public void execute() {
        String url = config.getUrl() + "/v1/cryptocurrency/listings/latest?start=1&limit=" + config.getListingLimit() + "&sort=market_cap&cryptocurrency_type=all&tag=all";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", config.getKey());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CMCResponseErrorHandler());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Map> LatestListingsResponse = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Map.class);
        fillDb(LatestListingsResponse);
    }

    @Transactional
    public void fillDb(ResponseEntity<Map> LatestListingsResponse) {
        ArrayList<LinkedHashMap> LatestListingsData = (ArrayList<LinkedHashMap>) LatestListingsResponse.getBody().get("data");
        ArrayList<LatestListings> latestListings= new ArrayList<>();
        try {
            for (LinkedHashMap data : LatestListingsData) {
                LatestListings listing = new LatestListings();
                listing.setId((Integer) data.get("id"));
                listing.setName((String) data.get("name"));
                listing.setSymbol((String) data.get("symbol"));
                listing.setSlug((String) data.get("slug"));
                listing.setRank((Integer) data.get("cmc_rank"));
                String lastUpdatedString = (String) data.get("last_updated");
                LinkedHashMap quotesDataPerCurrency = (LinkedHashMap) data.get("quote");
                LinkedHashMap quotesData = (LinkedHashMap) quotesDataPerCurrency.get("USD");
                listing.setLastUpdated(LocalDateTime.parse(lastUpdatedString.substring(0, lastUpdatedString.length() - 1)));
                listing.setMaxSupply(data.get("max_supply") == null ? null : ((Number) data.get("max_supply")).doubleValue());
                listing.setCirculatingSupply(data.get("circulating_supply") == null ? null : ((Number) data.get("circulating_supply")).doubleValue());
                listing.setTotalSupply(data.get("total_supply") == null ? null : ((Number) data.get("total_supply")).doubleValue());
                Optional<LatestListings> existingListing =null;
                try {
                    existingListing = repository.findById((Integer) data.get("id"));
                } catch (Exception ignore) {
//                    throw e;
                }
                if (existingListing.isEmpty()) {
                    listing.setQuotesUSD(quotesService.getQuotesObject(quotesData, new LatestQuotesUSD()));
                } else {
                    listing.setQuotesUSD(quotesService.getQuotesObject(quotesData, existingListing.get().getQuotesUSD()));
                }
                latestListings.add(listing);
            }
            repository.saveAll(latestListings);
        } catch (Exception e) {
            throw new ListingsNotFetched();
        }
    }

    public List<LatestListings> getListOfCoins(int pageNo, int count, String orderBy, Sort.Direction direction) {
        List<LatestListings> listings;
        try {
            listings = repository.findAll(PageRequest.of(pageNo - 1, count, Sort.by(direction, orderBy))).getContent();
            if (listings.isEmpty()) {
                throw new ListingsNotFoundException();
            }
        } catch (Exception e) {
            throw new ListingsNotFoundException();
        }
        return listings;
    }
}
