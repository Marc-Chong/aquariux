package com.Marc.Test.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="BALANCE")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    public Long Id;
    public Double balance;
    public String currency;
}
