package com.koinmarket.app.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class WatchlistDTO {
    private Integer id;
    private String name;

    private Set<ListingDTO> listing = new HashSet<>();
}
