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

import java.util.*;

@Service
public class MetadataService {

    @Autowired
    private MetadataRepository metadataRepository;
    @Autowired
    private AppAPIConfiguration config;

    public ResponseEntity<List<Metadata>> getMetadataById(List<Integer> ids) {

        List<Integer> idParameter = new ArrayList<>();
         for (Integer id : ids) {
             if (!metadataRepository.existsById(id)) {
                 idParameter.add(id);
             }
         }
        if (!idParameter.isEmpty()) getFromAPI(idParameter);
        List<Metadata> metadata= metadataRepository.findAllById(ids);
        return ResponseEntity.ok().body(metadata);
    }

    @Transactional
    public void fillDb(ResponseEntity<Map> metadataResponse, List<Integer> ids) {

        LinkedHashMap responseData = (LinkedHashMap) metadataResponse.getBody().get("data");

        ids.forEach(id -> {
            LinkedHashMap metadataData = (LinkedHashMap) responseData.get(Integer.toString(id));

            String name = (String) metadataData.get("name");
            String symbol = (String) metadataData.get("symbol");
            String category = (String) metadataData.get("category");
            String slug = (String) metadataData.get("slug");
            String description = (String) metadataData.get("description");
            String logoURL = (String) metadataData.get("logo");

            Metadata metadata = new Metadata(id, name, symbol, category, slug, description, logoURL);

            metadataRepository.save(metadata);
        });
    }

    public void getFromAPI(List<Integer> ids) {
        String idsAsString  = "";
        for (Integer id: ids) {
            if (idsAsString.isBlank()) idsAsString += id.toString();
            else idsAsString += "," + id.toString();
        }

        String url = config.getUrl() + "/v2/cryptocurrency/info?id=" + idsAsString;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", config.getKey());

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> metaDataResponse = restTemplate.exchange(url, HttpMethod.GET,  httpEntity, Map.class);
        fillDb(metaDataResponse, ids);
    }
}
