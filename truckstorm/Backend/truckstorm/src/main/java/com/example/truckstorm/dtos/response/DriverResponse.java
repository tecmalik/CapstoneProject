package com.example.truckstorm.dtos.response;

import com.example.truckstorm.data.models.TruckType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DriverResponse {
    private Integer id;
    private TruckType truckType;
    private Double maxLoadCapacity;
    private String driverStatus;
    private Boolean available;
    private String driverLicenseNumber;
    private Double rating;
    private String currentLocation;
}
