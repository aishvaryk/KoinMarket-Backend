package com.koinmarket.app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "id_map")
public class IDMap {

    @Id
    @Getter @Setter
    private int id;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String symbol;

    @Getter @Setter
    private String slug;

    @Getter @Setter
    private int rank;

    @Getter @Setter
    @Column(name = "display_tv")
    private int displayTV;

    @Getter @Setter
    @Column (name="manual_set_tv")
    private int manualSetTV;

    @Getter @Setter
    private String tvCoinSymbol;

    @Getter @Setter
    @Column(name="is_active")
    private int isActive;

    @Getter @Setter
    @Column(name="first_historical_data")
    private String firstHistoricalData;

    @Getter @Setter
    @Column(name="last_historical_data")
    private String lastHistoricalData;

//    @Getter @Setter
//    private String platform;
    public IDMap() {

    }
    public IDMap(int id, String name, String symbol, String slug, int rank, int displayTV, int manualSetTV, String tvCoinSymbol, int is_active, String first_historical_data, String last_historical_data) {
        super();
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.slug = slug;
        this.rank = rank;
        this.displayTV =displayTV;
        this.manualSetTV = manualSetTV;
        this.tvCoinSymbol = tvCoinSymbol;
        this.isActive = is_active;
        this.firstHistoricalData = first_historical_data;
        this.lastHistoricalData = last_historical_data;
//        this.platform = platform;
    }
}
