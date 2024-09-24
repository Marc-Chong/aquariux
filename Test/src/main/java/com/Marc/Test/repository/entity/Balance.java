package com.Marc.Test.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="BALANCE")
@Data
public class Balance {
    @Id

    String currency;
    Double balance;

    public Balance()
    {}

    public Balance(String otherCurrency, Double otherBalance)
    {
        currency = otherCurrency;
        balance = otherBalance;
    }
}
