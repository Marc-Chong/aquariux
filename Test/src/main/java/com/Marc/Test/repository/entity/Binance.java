package com.Marc.Test.repository.entity;

import lombok.Data;

@Data
public class Binance {
    String symbol;
    Double bidPrice;
    Double bidQty;
    Double askPrice;
    Double askQty;
}
