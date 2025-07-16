package com.example.truckstorm.data.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;


@Entity
@DiscriminatorValue("DRIVER")
@Getter
@Setter
public class Driver extends User {

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Truck type is required")
    @Column(nullable = false)
    private TruckType truckType;

    @NotNull(message = "Max load capacity is required")
    @Positive(message = "Max load capacity must be positive")
    @Column(nullable = false)
    private Double maxLoadCapacity;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean available = true;

    @NotBlank(message = "Driver license number is required")
    @Column(name = "driver_license_number", nullable = false, unique = true)
    private String driverLicenseNumber;

    @Column(columnDefinition = "DECIMAL(3,2) DEFAULT 5.0")
    @PositiveOrZero(message = "Rating must be positive or zero")
    private Double rating = 5.0;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id", referencedColumnName = "id")
    private Truck truck;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriverStatus status = DriverStatus.AVAILABLE;

    @Embedded
    private Location locationDetails;

    // Custom getter for currentLocation that converts Location to String
    public String getCurrentLocation() {
        if (locationDetails != null) {
            return locationDetails.getLatitude() + "," + locationDetails.getLongitude();
        }
        return super.getCurrentLocation();
    }

    // Custom setter for currentLocation that parses String to Location
    public void setCurrentLocation(String locationString) {
        if (locationString != null && !locationString.isEmpty()) {
            String[] parts = locationString.split(",");
            if (parts.length == 2) {
                try {
                    double lat = Double.parseDouble(parts[0].trim());
                    double lng = Double.parseDouble(parts[1].trim());
                    this.locationDetails = new Location(lat, lng, null);
                } catch (NumberFormatException e) {
                    // If parsing fails, fall back to parent class behavior
                    super.setCurrentLocation(locationString);
                }
            } else {
                // If format is not lat,lng, fall back to parent class behavior
                super.setCurrentLocation(locationString);
            }
        } else {
            super.setCurrentLocation(locationString);
        }
    }

    // Additional getter for Location object
    public Location getLocationDetails() {
        return locationDetails;
    }

    // Additional setter for Location object
    public void setLocationDetails(Location locationDetails) {
        this.locationDetails = locationDetails;
    }
}
