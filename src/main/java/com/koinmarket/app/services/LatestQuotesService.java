package com.koinmarket.app.services;

import com.koinmarket.app.AppAPIConfiguration;
import com.koinmarket.app.entities.LatestQuotesUSD;
import com.koinmarket.app.repositories.LatestQuotesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

        List<Integer> idParameter = new ArrayList<>();
         for (Integer id : ids) {
             if (!latestQuotesRepository.existsById(id)) {
                 idParameter.add(id);
             }
         }
        if (!idParameter.isEmpty()) getFromAPI(idParameter);
        List<LatestQuotesUSD> latestQuotes= latestQuotesRepository.findAllById(ids);
        return ResponseEntity.ok().body(latestQuotes);
    }

    @Transactional
    public void fillDb(ResponseEntity<Map> latestQuotesResponse, List<Integer> ids) {

        LinkedHashMap responseData = (LinkedHashMap) latestQuotesResponse.getBody().get("data");

        ids.forEach(id -> {
            LinkedHashMap responseDataPerID = (LinkedHashMap) responseData.get(Integer.toString(id));
            LinkedHashMap quotesDataPerCurrency = (LinkedHashMap) responseDataPerID.get("quote");
            LinkedHashMap quotesData= (LinkedHashMap) quotesDataPerCurrency.get("USD");

            Double price = ((Number) quotesData.get("price")).doubleValue();
            Double volume24H = ((Number) quotesData.get("volume_24h")).doubleValue();
            Double percentageChange1H = ((Number) quotesData.get("percent_change_1h")).doubleValue();
            Double percentageChange24H = ((Number) quotesData.get("percent_change_24h")).doubleValue();
            Double percentageChange7D = ((Number) quotesData.get("percent_change_7d")).doubleValue();
            Double percentageChange30D = ((Number) quotesData.get("percent_change_30d")).doubleValue();
            Double percentageChange60D = ((Number) quotesData.get("percent_change_60d")).doubleValue();
            Double percentageChange90D = ((Number) quotesData.get("percent_change_90d")).doubleValue();
            Double marketCap = ((Number) quotesData.get("market_cap")).doubleValue();
            Double marketCapDominance = ((Number) quotesData.get("market_cap_dominance")).doubleValue();
            Double fullyDilutedMarketCap = ((Number) quotesData.get("fully_diluted_market_cap")).doubleValue();
            String lastUpdated = (String) quotesData.get("last_updated");

            LatestQuotesUSD latestQuotesUSD = new LatestQuotesUSD(id, price, volume24H, percentageChange1H, percentageChange24H, percentageChange7D, percentageChange30D, percentageChange60D, percentageChange90D, marketCap, marketCapDominance, fullyDilutedMarketCap, lastUpdated);

            latestQuotesRepository.save(latestQuotesUSD);
        });
    }

    public void getFromAPI(List<Integer> ids) {
        String idsAsString  = "";
        for (Integer id: ids) {
            if (idsAsString.isBlank()) idsAsString += id.toString();
            else idsAsString += "," + id.toString();
        }
        String url = config.getUrl() + "/v2/cryptocurrency/quotes/latest?id=" + idsAsString;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", config.getKey());

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> latestQuotesResponse = restTemplate.exchange(url, HttpMethod.GET,  httpEntity, Map.class);
        fillDb(latestQuotesResponse, ids);
    }
}
