package com.example.truckstorm.data.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("DRIVER")
public class Driver extends User {

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Truck type is required")
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
    private Location driverLocation;
    
    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Manual getters and setters for compilation (in case @Data doesn't work)
    public Double getMaxLoadCapacity() { return maxLoadCapacity; }
    public void setMaxLoadCapacity(Double maxLoadCapacity) { this.maxLoadCapacity = maxLoadCapacity; }
    
    public TruckType getTruckType() { return truckType; }
    public void setTruckType(TruckType truckType) { this.truckType = truckType; }
    
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Location getDriverLocation() { return driverLocation; }
    public void setDriverLocation(Location driverLocation) { this.driverLocation = driverLocation; }
}
