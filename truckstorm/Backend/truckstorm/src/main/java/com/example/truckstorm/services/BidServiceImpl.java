package com.example.truckstorm.services;

import com.example.truckstorm.data.models.Driver;
import com.example.truckstorm.data.models.Load;
import com.example.truckstorm.data.models.LoadStatus;
import com.example.truckstorm.data.models.LoadType;
import com.example.truckstorm.data.models.TruckType;
import com.example.truckstorm.data.repository.DriverRepository;
import com.example.truckstorm.data.repository.LoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BidServiceImpl implements BidService {
    
    private final DriverRepository driverRepository;
    private final LoadRepository loadRepository;
    private final LoadService loadService;
    private final DriverService driverService;

    @Autowired
    public BidServiceImpl(DriverRepository driverRepository, 
                         LoadRepository loadRepository,
                         LoadService loadService,
                         DriverService driverService) {
        this.driverRepository = driverRepository;
        this.loadRepository = loadRepository;
        this.loadService = loadService;
        this.driverService = driverService;
    }

    @Override
    public List<Driver> findCompatibleDriversForLoad(Load load) {
        // For now, get all available drivers since the repository method might not exist yet
        List<Driver> availableDrivers = driverRepository.findByAvailable(true);

        return availableDrivers.stream()
                .filter(driver -> driver.getMaxLoadCapacity() >= load.getWeight())
                .filter(driver -> isTruckTypeCompatible(driver.getTruckType(), load.getLoadType()))
                .collect(Collectors.toList());
    }

    @Override
    public Driver assignDriverToLoad(Long loadId, Long driverId) {
        Load load = loadService.getLoadById(loadId);
        Driver driver = driverService.getDriverById(driverId);

        if (!findCompatibleDriversForLoad(load).contains(driver)) {
            throw new IllegalArgumentException("Driver is not compatible with this load");
        }

        load.setStatus(LoadStatus.ASSIGNED);
        load.setUpdatedAt(java.time.LocalDateTime.now());
        loadRepository.save(load);

        driver.setAvailable(false);
        driver.setUpdatedAt(java.time.LocalDateTime.now());
        return driverRepository.save(driver);
    }

    private boolean isTruckTypeCompatible(TruckType truckType, LoadType loadType) {
        if (loadType == LoadType.REFRIGERATED) {
            return truckType == TruckType.REEFER;
        }
        if (loadType == LoadType.HAZARDOUS) {
            return truckType == TruckType.TANKER || truckType == TruckType.FLATBED;
        }
        if (loadType == LoadType.OVERSIZED) {
            return truckType == TruckType.FLATBED || truckType == TruckType.LOWBOY;
        }
        // General freight can be handled by most truck types
        return true;
    }
}
