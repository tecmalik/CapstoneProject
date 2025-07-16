package com.example.truckstorm.services;

import com.example.truckstorm.data.models.Driver;
import com.example.truckstorm.data.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    @Autowired
    public DriverServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public Driver registerDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    @Override
    public Driver updateDriverAvailability(Long driverId, boolean available) {
        Driver driver = getDriverById(driverId);
        driver.setAvailable(available);
        driver.setUpdatedAt(LocalDateTime.now());
        return driverRepository.save(driver);
    }

    @Override
    public List<Driver> findAvailableDriversInRegion(String region) {
        // Since the repository method might use String instead of Location, keeping this for now
        return driverRepository.findByCurrentLocationAndAvailable(region, true);
    }

    @Override
    public Driver getDriverById(Long id) {
        Optional<Driver> driver = driverRepository.findById(id);
        return driver.orElseThrow(() -> new RuntimeException("Driver not found with id: " + id));
    }

    @Override
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }
}
