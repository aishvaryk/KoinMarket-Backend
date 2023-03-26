package com.koinmarket.app.services;

import com.koinmarket.app.configurations.AppAPIConfiguration;
import com.koinmarket.app.entities.LatestQuotesUSD;
import com.koinmarket.app.exceptions.CMCResponseErrorHandler;
import com.koinmarket.app.exceptions.quotes.QuotesNotFetched;
import com.koinmarket.app.exceptions.quotes.QuotesNotFoundException;
import com.koinmarket.app.repositories.LatestQuotesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class LatestQuotesService {

    @Autowired
    private LatestQuotesRepository latestQuotesRepository;

    @Autowired
    private AppAPIConfiguration config;

    public ResponseEntity<List<LatestQuotesUSD>> getQuotesById(List<Integer> ids) {

        List<LatestQuotesUSD> latestQuotes;
        try {
            List<Integer> idParameter = new ArrayList<>();
            for (Integer id : ids) {
                if (!latestQuotesRepository.existsById(id)) {
                    idParameter.add(id);
                }
            }
            if (!idParameter.isEmpty()) getFromAPI(idParameter);
            latestQuotes = latestQuotesRepository.findAllById(ids);
        } catch (Exception e) {
            throw new QuotesNotFetched();
        }
        if (latestQuotes.isEmpty()) {
            throw new QuotesNotFoundException();
        }
        return ResponseEntity.ok().body(latestQuotes);
    }

    @Transactional
    private void readQuotesResponse(ResponseEntity<Map> latestQuotesResponse, List<Integer> ids) {

        LinkedHashMap responseData = (LinkedHashMap) latestQuotesResponse.getBody().get("data");

        ids.forEach(id -> {
            LinkedHashMap responseDataPerID = (LinkedHashMap) responseData.get(Integer.toString(id));
            LinkedHashMap quotesDataPerCurrency = (LinkedHashMap) responseDataPerID.get("quote");
            LinkedHashMap quotesData = (LinkedHashMap) quotesDataPerCurrency.get("USD");
            LatestQuotesUSD latestQuotesUSD = getQuotesObject(quotesData, new LatestQuotesUSD());
            latestQuotesRepository.save(latestQuotesUSD);
        });
    }


    public LatestQuotesUSD getQuotesObject(LinkedHashMap quotesData, LatestQuotesUSD latestQuotesObject) {

        latestQuotesObject.setPrice(((Number) quotesData.get("price")).doubleValue());
        latestQuotesObject.setVolume24H(((Number) quotesData.get("volume_24h")).doubleValue());
        latestQuotesObject.setVolumeChange24H(((Number) quotesData.get("volume_change_24h")).doubleValue());
        latestQuotesObject.setPercentChange1H(((Number) quotesData.get("percent_change_1h")).doubleValue());
        latestQuotesObject.setPercentChange24H(((Number) quotesData.get("percent_change_24h")).doubleValue());
        latestQuotesObject.setPercentChange7D(((Number) quotesData.get("percent_change_7d")).doubleValue());
        latestQuotesObject.setPercentChange30D(((Number) quotesData.get("percent_change_30d")).doubleValue());
        latestQuotesObject.setPercentChange60D(((Number) quotesData.get("percent_change_60d")).doubleValue());
        latestQuotesObject.setPercentChange90D(((Number) quotesData.get("percent_change_90d")).doubleValue());
        latestQuotesObject.setMarketCap(((Number) quotesData.get("market_cap")).doubleValue());
        latestQuotesObject.setMarketCapDominance(((Number) quotesData.get("market_cap_dominance")).doubleValue());
        latestQuotesObject.setFullyDilutedMarketCap(((Number) quotesData.get("fully_diluted_market_cap")).doubleValue());
        String lastUpdatedString = (String) quotesData.get("last_updated");
        latestQuotesObject.setLastUpdated(LocalDateTime.parse(lastUpdatedString.substring(0, lastUpdatedString.length() - 1)));

        return latestQuotesObject;
    }

    private void getFromAPI(List<Integer> ids) {
        String idsAsString = "";
        for (Integer id : ids) {
            if (idsAsString.isBlank()) idsAsString += id.toString();
            else idsAsString += "," + id.toString();
        }
        String url = config.getUrl() + "/v2/cryptocurrency/quotes/latest?id=" + idsAsString;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", config.getKey());

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CMCResponseErrorHandler());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> latestQuotesResponse = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Map.class);
        readQuotesResponse(latestQuotesResponse, ids);
    }
}
