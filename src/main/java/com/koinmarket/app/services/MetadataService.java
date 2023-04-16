package com.koinmarket.app.services;

import com.koinmarket.app.configurations.AppAPIConfiguration;
import com.koinmarket.app.entities.LatestListings;
import com.koinmarket.app.entities.Metadata;
import com.koinmarket.app.exceptions.CMCResponseErrorHandler;
import com.koinmarket.app.exceptions.metadata.MetadataNotFetched;
import com.koinmarket.app.repositories.LatestListingRepository;
import com.koinmarket.app.repositories.MetadataRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class MetadataService {

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private LatestListingRepository latestListingRepository;

    @Autowired
    private AppAPIConfiguration config;

    public List<Metadata> getMetadataById(List<Integer> ids) {

        List<Integer> idParameter = new ArrayList<>();
        for (Integer id : ids) {
            if (!metadataRepository.existsById(id)) {
                idParameter.add(id);
            }
        }
        if (!idParameter.isEmpty()) getFromAPI(idParameter);
        List<Metadata> metadata = metadataRepository.findAllById(ids);
        return metadata;
    }

    @Transactional
    public void fillDb(ResponseEntity<Map> metadataResponse, List<Integer> ids) {

        LinkedHashMap responseData = (LinkedHashMap) metadataResponse.getBody().get("data");

        ids.forEach(id -> {
            LinkedHashMap metadataData = (LinkedHashMap) responseData.get(Integer.toString(id));

            Metadata metadata = new Metadata();
            metadata.setId(id);
            metadata.setSymbol((String) metadataData.get("symbol"));
            metadata.setCategory((String) metadataData.get("category"));
            metadata.setDescription((String) metadataData.get("description"));
            metadata.setLogoURL((String) metadataData.get("logo"));
            LinkedHashMap urls = (LinkedHashMap) metadataData.get("urls");
            ArrayList<String> websiteResponse = (ArrayList<String>) urls.get("website");
            metadata.setWebsite(websiteResponse.isEmpty() ? null : websiteResponse.get(0));
            ArrayList<String> twitterResponse = (ArrayList<String>) urls.get("twitter");
            metadata.setTwitter(twitterResponse.isEmpty() ? null : twitterResponse.get(0));
            ArrayList<String> redditResponse = (ArrayList<String>) urls.get("reddit");
            metadata.setReddit(redditResponse.isEmpty() ? null : redditResponse.get(0));
            LatestListings latestListings = latestListingRepository.findById(id).orElse(null);
            if (latestListings != null) {
                metadata.setLatestListings(latestListings);
                latestListings.setMetadata(metadata);
                latestListingRepository.save(latestListings);
            } else {
                throw new MetadataNotFetched();
            }
        });
    }

    public void getFromAPI(List<Integer> ids) {
        String idsAsString = "";
        for (Integer id : ids) {
            if (idsAsString.isBlank()) idsAsString += id.toString();
            else idsAsString += "," + id.toString();
        }

        String url = config.getUrl() + "/v2/cryptocurrency/info?id=" + idsAsString;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", config.getKey());

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CMCResponseErrorHandler());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> metaDataResponse = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Map.class);
        fillDb(metaDataResponse, ids);
    }
}
