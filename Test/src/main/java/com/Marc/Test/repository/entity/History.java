package com.Marc.Test.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name="HISTORY")
@Data
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    public Long Id;
    public Date time;
    public String buySell;
    public Double balance;
    public String currency;
}
