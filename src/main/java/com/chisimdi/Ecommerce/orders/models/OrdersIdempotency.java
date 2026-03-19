package com.chisimdi.Ecommerce.orders.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrdersIdempotency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Orders orders;
    @Column(unique = true)
    private String idempotencyKey;
}
