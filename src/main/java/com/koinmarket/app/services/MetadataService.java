package com.koinmarket.app.services;

import com.koinmarket.app.AppAPIConfiguration;
import com.koinmarket.app.entities.Metadata;
import com.koinmarket.app.repositories.MetadataRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class MetadataService {

    @Autowired
    private MetadataRepository metadataRepository;
    @Autowired
    private AppAPIConfiguration config;

    public ResponseEntity<Metadata> getMetadataById(int id) {
        Optional<Metadata> metadata= metadataRepository.findById(id);
        if (metadata.isPresent()) {
            return ResponseEntity.ok().body(metadata.get());
        }
        else {
            getFromAPI(id);
            return ResponseEntity.ok().body(metadata.get());
        }
    }

    @Transactional
    public void fillDb(ResponseEntity<Map> metadataResponse, int id) {
        System.out.println("@@@@@@@ " + metadataResponse);
        LinkedHashMap responseData = (LinkedHashMap) metadataResponse.getBody().get("data");
        LinkedHashMap metadataData = (LinkedHashMap) responseData.get(Integer.toString(id));
        String name = (String) metadataData.get("name");
        String symbol = (String) metadataData.get("symbol");
        String category = (String) metadataData.get("category");
        String slug = (String) metadataData.get("slug");
        String description  = (String) metadataData.get("description");
        String logoURL = (String) metadataData.get("logo");
        Metadata metadata = new Metadata(id, name, symbol, category, slug, description, logoURL);
        metadataRepository.save(metadata);
    }

    public void getFromAPI(int id) {
        String url = config.getUrl() + "/v2/cryptocurrency/info?id=" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", config.getKey());
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Map> metaDataResponse = restTemplate.exchange(url, HttpMethod.GET,  httpEntity, Map.class);
        fillDb(metaDataResponse, id);
    }
}
