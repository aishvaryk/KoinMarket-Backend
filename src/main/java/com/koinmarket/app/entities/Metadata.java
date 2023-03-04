package com.koinmarket.app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Metadata")
public class Metadata {

    @Getter @Setter
    @Id
    private int id;
    @Getter @Setter
    private String name;

    @Getter @Setter
    private String symbol;

    @Getter @Setter
    private String category;

    @Getter @Setter
    private String slug;

    @Getter @Setter
    @Column(length = 1024)
    private String description;

    @Getter @Setter
    @Column(name="logo_URL")
    private String logoURL;

    public Metadata() {};

    public Metadata(int id, String name, String symbol, String category, String slug, String description, String logoURL) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.category =category;
        this.slug = slug;
        this.description = description;
        this.logoURL = logoURL;
    }
}
