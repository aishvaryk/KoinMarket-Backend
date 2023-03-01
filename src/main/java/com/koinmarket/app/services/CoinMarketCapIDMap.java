package com.koinmarket.app.services;


import com.koinmarket.app.AppAPIConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class CoinMarketCapIDMap {

    @Autowired
    private AppAPIConfiguration config;

    @Scheduled(fixedDelay = 3600000)
    public void execute() {
        String url = config.getUrl() + "/v1/cryptocurrency/map?start=1&limit=100&sort=cmc_rank";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", config.getKey());
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Map> IDMap = restTemplate.exchange(url, HttpMethod.GET,  httpEntity, Map.class);
//        System.out.println(response.getBody());
    }

}
