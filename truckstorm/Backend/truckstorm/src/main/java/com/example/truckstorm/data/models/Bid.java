package com.example.truckstorm.data.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "bids")
@Getter
@Setter
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private BidStatus bidStatus;


    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "load_id")
    private Load load;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    private LocalDateTime proposedPickupTime;

    private Instant bidTimestamp;

    private Instant expiryTime;
    @Column(length = 1000)
    private String note;
}
