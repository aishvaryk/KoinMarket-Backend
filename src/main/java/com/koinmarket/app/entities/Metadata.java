package com.koinmarket.app.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "Metadata")
public class Metadata {


    @Id
    @Column(name = "latest_listing_id")
    private Integer id;

    private String category;

    @Column(length = 1024, nullable = true)
    private String description;

    @Column(name="logo_URL")
    private String logoURL;

    private String website;

    private String reddit;

    private String twitter;

    @OneToOne
    @JoinColumn(name = "latest_listing_id")
    @MapsId
    @JsonBackReference
    private LatestListings latestListings;

    public Metadata() {
        super();
    }
}
