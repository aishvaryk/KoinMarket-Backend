package com.koinmarket.app.services;


import com.koinmarket.app.AppAPIConfiguration;
import com.koinmarket.app.entities.IDMap;
import com.koinmarket.app.repositories.IDMapRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class IDMapService {

    @Autowired
    private IDMapRepository repository;
    @Autowired
    private AppAPIConfiguration config;

    @Scheduled(fixedDelay = 3600000)
    public void execute() {
        String url = config.getUrl() + "/v1/cryptocurrency/map?start=1&limit=5000&sort=cmc_rank";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", config.getKey());
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Map> IDMap = restTemplate.exchange(url, HttpMethod.GET,  httpEntity, Map.class);
        fillDb(IDMap);
    }

    @Transactional
    public void fillDb(ResponseEntity<Map> IDMap) {
        ArrayList<LinkedHashMap> IDMapData = (ArrayList<LinkedHashMap>) IDMap.getBody().get("data");
        for (LinkedHashMap data: IDMapData) {
            System.out.println("***********************************************************************************************************");
            IDMap map = new IDMap((Integer) data.get("id"), (String) data.get("name"), (String) data.get("symbol"), (String) data.get("slug"), (Integer) data.get("rank"), (Integer) data.get("displayTV"), (Integer) data.get("manualSetTV"), (String) data.get("tvCoinSymbol"), (Integer) data.get("is_active"), (String) data.get("first_historical_data"), (String) data.get("last_historical_data"));
            repository.save(map);
        }
    }

    public List<IDMap> getListOfCoins(int pageNo, int count, String orderBy, String direction) {
        if (direction.equals("desc")) return repository.findAll(PageRequest.of(pageNo-1, count, Sort.by(Sort.Direction.DESC, orderBy))).getContent();
        return repository.findAll(PageRequest.of(pageNo-1, count, Sort.by(Sort.Direction.ASC, orderBy))).getContent();
    }
}
