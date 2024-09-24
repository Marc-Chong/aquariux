package com.Marc.Test.repository.entity;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class Huobi {
    String symbol;
    Double open;
    Double high;
    Double low;
    Double close;
    Double amount;
    Double vol;
    Double count;
    Double bid;
    Double bidSize;
    Double ask;
    Double askSize;
}
