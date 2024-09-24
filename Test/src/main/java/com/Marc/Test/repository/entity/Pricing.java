package com.Marc.Test.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="PRICING")
@Data
public class Pricing {
    @Id

    String symbol;
    Double bidPrice;
    Double askPrice;

    Pricing()
    {}

    public Pricing(String otherSymbol, Double otherAskPrice, Double otherBidPrice)
    {
        symbol = otherSymbol;
        askPrice = otherAskPrice;
        bidPrice = otherBidPrice;
    }
}
