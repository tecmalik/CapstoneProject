package com.example.truckstorm.data.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Truck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "License plate is required")
    @Column(unique = true)
    private String truckLicensePlateNumber;

    @NotNull(message = "Capacity is required")
    private Double capacity;


    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Enumerated(EnumType.STRING)
    private TruckType truckType;

    @Enumerated(EnumType.STRING)
    private TruckStatus truckStatus;
}
