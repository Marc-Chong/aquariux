package com.Marc.Test.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="HISTORY")
@Data
public class History {
    @Id

   LocalDateTime time;
   String buySell;
   Double fromBalance;
   String fromCurrency;
   Double toBalance;
   String toCurrency;

    History()
    {}

    public History(LocalDateTime otherTime, String otherBuySell, Double otherFromBalance, String otherFromCurrency,
            Double otherToBalance, String otherToCurrency)
    {
        time = otherTime;
        buySell = otherBuySell;
        fromBalance = otherFromBalance;
        fromCurrency = otherFromCurrency;
        toBalance = otherToBalance;
        toCurrency = otherToCurrency;
    }
}
